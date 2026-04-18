import sys
import xml.etree.ElementTree as ET
import yaml
from pathlib import Path

if len(sys.argv) < 3:
    raise Exception("Usage: generate_helm_values.py <pom_file> <output_file>")

POM_FILE = sys.argv[1]
OUTPUT_FILE = Path(sys.argv[2])


def convert_value(value):
    v = value.strip()
    if v.lower() in ("true", "false"):
        return v.lower() == "true"
    if v.isdigit():
        return int(v)
    try:
        return float(v)
    except:
        return v


def strip_ns(tag):
    return tag.split("}", 1)[-1]


def extract_helm_properties(pom_path):
    tree = ET.parse(pom_path)
    root = tree.getroot()
    result = {}
    for prop in root.iter():
        key = strip_ns(prop.tag)
        value = (prop.text or "").strip()
        value = " ".join(value.split())
        if key.startswith("helm."):
            clean_key = key.replace("helm.", "", 1)
            result[clean_key] = convert_value(value)
    return result


def build_nested_yaml(flat_dict):
    result = {}
    for key, value in flat_dict.items():
        parts = key.split(".")
        current = result
        for i, part in enumerate(parts):
            if i == len(parts) - 1:
                current[part] = value
            else:
                current = current.setdefault(part, {})
    return result


def write_values_yaml(config: dict, output_path: Path):
    output_path.parent.mkdir(parents=True, exist_ok=True)
    yaml_data = build_nested_yaml(config)
    with open(output_path, "w", encoding="utf-8") as f:
        yaml.dump(yaml_data, f, sort_keys=False)
    print(f"Generated: {output_path}")


if __name__ == "__main__":
    config = extract_helm_properties(POM_FILE)
    write_values_yaml(config, OUTPUT_FILE)

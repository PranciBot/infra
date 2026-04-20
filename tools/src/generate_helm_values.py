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


def parse_env_block(env_str):
    env_list = []
    lines = [line.strip() for line in env_str.splitlines() if line.strip()]

    for line in lines:
        if "=" not in line:
            continue
        key, value = line.split("=", 1)

        # convert to ENV format: plane.api-url → PLANE_API_URL
        env_name = key.upper().replace(".", "_").replace("-", "_")

        env_list.append({
            "name": env_name,
            "value": value.strip()
        })

    return env_list


def extract_helm_properties(pom_path):
    tree = ET.parse(pom_path)
    root = tree.getroot()
    result = {}

    for prop in root.iter():
        key = strip_ns(prop.tag)
        raw_value = prop.text or ""

        if key.startswith("helm."):
            clean_key = key.replace("helm.", "", 1)

            if clean_key.endswith(".env"):
                result[clean_key] = parse_env_block(raw_value)
            else:
                value = " ".join(raw_value.split())
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

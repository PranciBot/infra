#!/bin/bash
chartDir=$1

echo "Start setup infra"

helm upgrade --install infra $chartDir \
    --create-namespace \
    --namespace default \
    --timeout 10m \
    --wait \
    --wait-for-jobs
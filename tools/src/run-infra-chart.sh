#!/bin/bash

echo "Start setup infra"

helm upgrade --install infra ../../chart/ \
    --create-namespace \
    --namespace default \
    --timeout 10m \
    --wait \
    --wait-for-jobs
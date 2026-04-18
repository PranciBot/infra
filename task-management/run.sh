#!/bin/bash
PLANE_VERSION=v2.4.0
DOMAIN_NAME=plane.127.0.0.1.sslip.io
NAMESPACE=default

echo "Start setup [plane:$PLANE_VERSION] at $DOMAIN_NAME"

helm upgrade --install plane plane/plane-enterprise \
    --create-namespace \
    --namespace $NAMESPACE \
    --set license.licenseDomain=$DOMAIN_NAME \
    --set license.licenseServer=https://prime.plane.so \
    --set planeVersion=$PLANE_VERSION \
    --set ingress.enabled=true \
    --set ingress.ingressClass=kong \
    --set env.storageClass=longhorn \
    --timeout 10m \
    --wait \
    --wait-for-jobs
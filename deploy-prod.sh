#!/bin/bash
kubectl apply -f kubernetes/volumes/mongo.yaml
kubectl apply -f kubernetes/volumes/elasticsearch.yaml 

kubectl apply -f kubernetes/ConfigMaps/web-scraping-service-config-map.yaml 
kubectl apply -f kubernetes/ConfigMaps/spark-manager-service-config-map.yaml
kubectl apply -f kubernetes/ConfigMaps/search-service-config-map.yaml 
kubectl apply -f kubernetes/ConfigMaps/notification-service-config-map.yaml
kubectl apply -f kubernetes/ConfigMaps/hystrix-dashboard-config-map.yaml
kubectl apply -f kubernetes/ConfigMaps/hadoop-docker-node-config-map.yaml 
kubectl apply -f kubernetes/ConfigMaps/ftp-explorer-service-config-map.yaml
kubectl apply -f kubernetes/ConfigMaps/file-management-service-config-map.yaml 


kubectl apply -f kubernetes/elasticsearch-service-statefulset.yaml 
kubectl apply -f kubernetes/mongo-service-statefulset.yaml 

kubectl apply -f kubernetes/redis-service-deployment.yaml 
kubectl apply -f kubernetes/tika-service-deployment.yaml 
kubectl apply -f kubernetes/rabbitmq-service-deployment.yaml 
kubectl apply -f kubernetes/web-scraping-service-deployment.yaml 
kubectl apply -f kubernetes/spark-manager-service-service-deployment.yaml
kubectl apply -f kubernetes/notification-service-service-deployment.yaml
kubectl apply -f kubernetes/hystrix-dashboard-service-deployment.yaml
kubectl apply -f kubernetes/ftp-explorer-service-deployment.yaml
kubectl apply -f kubernetes/front-end-service-deployment.yaml
kubectl apply -f kubernetes/search-service-service-deployment.yaml
kubectl apply -f kubernetes/file-management-service-service-deployment.yaml
kubectl apply -f kubernetes/hadoop-docker-node-service-deployment.yaml

kubectl create -f https://raw.githubusercontent.com/jaegertracing/jaeger-kubernetes/master/all-in-one/jaeger-all-in-one-template.yml

kubectl apply -f kubernetes/traefik/deployments.yaml 
kubectl apply -f kubernetes/traefik/services.yaml
kubectl apply -f kubernetes/traefik/ingress.yaml 
kubectl apply -f kubernetes/traefik/routes.yaml

kubectl apply -f "https://cloud.weave.works/k8s/scope.yaml?k8s-version=$(kubectl version | base64 | tr -d '\n')"

kubectl apply -f kubernetes/kibana-service-deployment.yaml
kubectl apply -f kubernetes/logstash-service-deployment.yam
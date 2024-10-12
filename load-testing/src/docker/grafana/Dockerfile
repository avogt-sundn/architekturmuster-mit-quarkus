FROM grafana/grafana:8.5.21

COPY grafana-dashboard.yaml /etc/grafana/provisioning/dashboards/main.yaml
COPY grafana-datasource.yaml /etc/grafana/provisioning/datasources/datasource.yaml
COPY k6-load-testing-results_rev3.json /var/lib/grafana/dashboards/k6-load-testing.json

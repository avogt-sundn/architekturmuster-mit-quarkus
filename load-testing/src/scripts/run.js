import { check, sleep } from "k6";
import http from 'k6/http';



const isNumeric = (value) => /^\d+$/.test(value);

const default_vus = 20;

const target_vus_env = `${__ENV.TARGET_VUS}`;
const target_vus = isNumeric(target_vus_env) ? Number(target_vus_env) : default_vus;

const service_url_env = `${__ENV.SERVICE_URL}`;
const service_url = service_url_env === 'undefined' ? "http://localhost:8080" : service_url_env;
// console.log(`Service URL: >${service_url}<`);

const path_env = `${__ENV.URL_PATH}`;
const path = path_env === 'undefined' ? "/resourcewithmetrics/virtual" : path_env;
// console.log(`Path: >${path}<`);

const url = service_url + path;
console.log(`Url: >${url}<`);

export const options = {
    stages: [
        // Ramp-up from 1 to TARGET_VUS virtual users (VUs) in 5s
        { duration: "5s", target: target_vus },

        // Stay at rest on TARGET_VUS VUs for 50s
        { duration: "5s", target: target_vus },

        // Ramp-down from TARGET_VUS to 0 VUs for 5s
        { duration: "1s", target: 0 }
    ]
};

export default function () {

    // import console output to see the path
    const r0 = http.get(url, { headers: { Accepts: "application/json" } });
    check(r0, { "status is 200": (r) => r.status === 200 });
}

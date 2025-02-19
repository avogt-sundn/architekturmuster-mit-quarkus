import { check } from "k6";
import http from 'k6/http';

import { randomItem } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';


const isNumeric = (value) => /^\d+$/.test(value);

const default_vus = 10;

const target_vus_env = `${__ENV.TARGET_VUS}`;
const target_vus = isNumeric(target_vus_env) ? Number(target_vus_env) : default_vus;

const service_url_env = `${__ENV.SERVICE_URL}`;
const service_url = service_url_env === 'undefined' ? "http://localhost:8080" : service_url_env;
// console.log(`Service URL: >${service_url}<`);

const path_env = `${__ENV.URL_PATH}`;
const path = path_env === 'undefined' ? "/resourcewithmetrics/virtual" : path_env;
// console.log(`Path: >${path}<`);

const url = service_url + path;
// console.log(`Url: >${url}<`);

export const options = {
    thresholds: {
        http_req_failed: ['rate<0.01'], // http errors should be less than 1%
        http_req_duration: ['p(95)<200'], // 95% of requests should be below 200ms
    },
    // Key configurations for Stress in this section
    stages: [
        { duration: '5s', target: target_vus }, // traffic ramp-up from 1 to a higher 200 users over 10 minutes.
        { duration: '10s', target: target_vus }, // stay at higher 200 users for 30 minutes
        { duration: '5s', target: 0 }, // ramp-down to 0 users
    ],
};

export default function () {

    // import console output to see the path
    const r0 = http.get(url, { headers: { Accepts: "application/json" } });
    check(r0, { "status is 200": (r) => r.status === 200 });
}

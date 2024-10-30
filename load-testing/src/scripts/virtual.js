import { check, sleep } from "k6";
import http from 'k6/http';



const isNumeric = (value) => /^\d+$/.test(value);

const default_vus = 50;

const target_vus_env = `${__ENV.TARGET_VUS}`;
const target_vus = isNumeric(target_vus_env) ? Number(target_vus_env) : default_vus;

export const options = {
    stages: [
        // Ramp-up from 1 to TARGET_VUS virtual users (VUs) in 5s
        { duration: "5s", target: target_vus },

        // Stay at rest on TARGET_VUS VUs for 50s
        { duration: "3s", target: target_vus },

        // Stay at rest on TARGET_VUS VUs for 50s
        { duration: "1s", target: target_vus * 4 },

        // Stay at rest on TARGET_VUS VUs for 50s
        { duration: "10s", target: target_vus },

        // Ramp-down from TARGET_VUS to 0 VUs for 5s
        { duration: "3s", target: 0 }
    ]
};

export default function () {
    const url = "http://host.docker.internal:8080/jsonresource/virtual";

    const r0 = http.get(url, { headers: { Accepts: "application/json" } });
    check(r0, { "status is 200": (r) => r.status === 200 });
    sleep(.100);

}

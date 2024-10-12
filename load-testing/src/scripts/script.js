import { check, sleep } from "k6";
import http from 'k6/http';

const isNumeric = (value) => /^\d+$/.test(value);

const default_vus = 50;

const target_vus_env = `${__ENV.TARGET_VUS}`;
const target_vus = isNumeric(target_vus_env) ? Number(target_vus_env) : default_vus;

export const options = {

    scenarios: {
        virtual: {
            executor: 'constant-arrival-rate',
            exec: 'virtual',
            preAllocatedVUs: 50,
            rate: 50,
        },
        //scenario to view news
        classic: {
            executor: 'constant-arrival-rate',
            exec: 'classic',
            preAllocatedVUs: 50,
            rate: 50,
        },
    },

};

export default function testSuite() {
    const r0 = http.get(url, { headers: { Accepts: "application/json" } });
    check(r0, { "status is 200": (r) => r.status === 200 });
    sleep(.100);
}

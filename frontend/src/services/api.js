import axios from "axios";
import keycloak from "./keycloak";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  async (config) => {
    if (keycloak.authenticated) {
      try {
        await keycloak.updateToken(30);

        config.headers.Authorization = `Bearer ${keycloak.token}`;
      } catch (error) {
        console.error("Failed to refresh Keycloak token:", error);
      }
    }

    return config;
  },
  (error) => Promise.reject(error),
);

export default api;

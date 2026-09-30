import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8180",
  realm: "ecommerce",
  clientId: "ecommerce-frontend",
});

export default keycloak;

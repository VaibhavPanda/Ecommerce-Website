import api from "./api";

export const getCurrentUser = async () => {
  return api.get("/auth/me");
};

import api from "./api";

export const getAdminUsers = () => {
  return api.get("/admin/users");
};

export const makeTenant = (userId, tenantData) => {
  return api.patch(`/admin/users/${userId}/make-tenant`, tenantData);
};

export const removeTenant = (userId) => {
  return api.patch(`/admin/users/${userId}/remove-tenant`);
};

// GET / api / admin / users;
// PATCH / api / admin / users / { id } / make - tenant;
// PATCH / api / admin / users / { id } / remove - tenant;

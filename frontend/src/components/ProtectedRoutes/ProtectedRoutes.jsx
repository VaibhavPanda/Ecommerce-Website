import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

function ProtectedRoute({ allowedRoles }) {
  const { initialized, authenticated, user } = useAuth();
  const location = useLocation();

  console.log({
    initialized,
    authenticated,
    user,
    allowedRoles,
  });

  // Keycloak is still initializing
  if (!initialized) {
    return <div>Loading...</div>;
  }

  // User is not logged in
  if (!authenticated) {
    return <Navigate to="/" replace state={{ from: location }} />;
  }

  // Route requires specific roles
  if (allowedRoles && !allowedRoles.includes(user?.role)) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
}

export default ProtectedRoute;

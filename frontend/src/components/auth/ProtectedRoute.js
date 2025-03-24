import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

const ProtectedRoute = ({ allowedRoles, redirectPath = "/" }) => {

  const token = localStorage.getItem("token");
  let userRole = null;
  let isValidToken = false;
  
  if (token) {
    try {
      const decodedToken = jwtDecode(token);
      userRole = decodedToken.role || null;
    } catch (error) {
        console.log(error);
        isValidToken = true;
    }
  }

  const roles = Array.isArray(allowedRoles) ? allowedRoles : [allowedRoles];

  const hasRequiredRole = userRole && roles.includes(userRole);

  if (!token || !hasRequiredRole || isValidToken) {
    return <Navigate to={redirectPath} replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
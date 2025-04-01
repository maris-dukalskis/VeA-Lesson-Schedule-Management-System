import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { CancelTokenAutoRefresh } from './TokenRefresh';

const Logout = () => {
  const navigate = useNavigate();

  useEffect(() => {
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");

    CancelTokenAutoRefresh();
    navigate("/");
  }, [navigate]);

  return (
    <>
    </>
  );
};

export default Logout;

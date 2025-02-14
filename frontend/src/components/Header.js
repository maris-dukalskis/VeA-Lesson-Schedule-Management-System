import React from "react";
import { Navbar, Button, Container } from "react-bootstrap";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from "react-router-dom";
import logo from "../resources/logo.svg";
import Login from "./auth/Login";

const Header = () => {
    const navigate = useNavigate();
    const token = localStorage.getItem("token");
    let userRole = null;
    let userName = null;

    if (token) {
        try {
            const decodedToken = jwtDecode(token);
            userRole = decodedToken.role || null;
            userName = decodedToken.name || "User";
        } catch (error) {
            console.error("Invalid token:", error);
        }
    }

    const isAuthenticated = !!token;
    const isAdmin = userRole === "ADMINISTRATOR";
    const isEditor = userRole === "EDITOR";

    const buttonStyle = {
        backgroundColor: "#145A32",
        borderColor: "#145A32",
    };

    return (
        <Navbar bg="dark" variant="dark" expand="lg" className="px-3 shadow-sm">
            <Container className="d-flex justify-content-between align-items-center w-100">
                <Button
                    style={buttonStyle}
                    className="me-3"
                    onClick={() => navigate("/")}
                >
                    Home
                </Button>

                <Navbar.Brand href="/" className="mx-auto">
                    <img src={logo} alt="Logo" style={{ height: "50px" }} />
                </Navbar.Brand>

                <div className="d-flex align-items-center">
                    {isAuthenticated && (
                        <span className="text-light me-3">
                            Welcome, <strong>{userName}</strong>
                        </span>
                    )}
                    {!isAuthenticated ? (
                        <Login />
                    ) : (
                        <>
                            {isAdmin && (
                                <Button
                                    style={buttonStyle}
                                    className="me-2"
                                    onClick={() => navigate("/admin")}
                                >
                                    Admin Panel
                                </Button>
                            )}
                            {isEditor && (
                                <Button
                                    style={buttonStyle}
                                    className="me-2"
                                    onClick={() => navigate("/edit")}
                                >
                                    Edit
                                </Button>
                            )}
                            <Button style={buttonStyle} onClick={() => navigate("/logout")}>
                                Logout
                            </Button>
                        </>
                    )}
                </div>
            </Container>
        </Navbar>
    );
};

export default Header;
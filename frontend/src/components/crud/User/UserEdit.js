import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import userServiceInstance from "../../../api/UserService";
import lecturerServiceInstance from "../../../api/LecturerService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";

const UserEdit = () => {
    const { id } = useParams();
    const [userType, setUserType] = useState("user");
    const [formData, setFormData] = useState({
        fullName: "",
        email: "",
        role: "USER",
        hours: ""
    });
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await userServiceInstance.getById(id);
                setFormData(prevData => ({
                    ...prevData,
                    fullName: response.data.fullName || "",
                    email: response.data.email || "",
                    role: response.data.role || "USER",
                    hours: response.data.hours || ""
                }));
                setUserType(response.data.hours ? "lecturer" : "user");
            } catch (error) {
                setMessage("Error fetching user details.");
            } finally {
                setLoading(false);
            }
        };

        fetchUserData();
    }, [id]);

    const handleChange = (event) => {
        setFormData(prevData => ({ ...prevData, [event.target.name]: event.target.value }));
    };

    const handleUserTypeChange = (event) => {
        const newUserType = event.target.value;
        setUserType(newUserType);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            if (userType === "lecturer") {
                await lecturerServiceInstance.update(id, {
                    fullName: formData.fullName,
                    email: formData.email,
                    role: formData.role,
                    hours: parseInt(formData.hours, 10)
                });
            } else {
                await userServiceInstance.update(id, {
                    fullName: formData.fullName,
                    email: formData.email,
                    role: formData.role
                });
            }
            setMessage("User updated successfully!");
        } catch (error) {
            setMessage("Error updating user. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Edit User</h2>
                    {message && <Alert variant={message.includes("Error") ? "danger" : "success"}>{message}</Alert>}
                    {loading ? (
                        <p className="text-center">Loading...</p>
                    ) : (
                        <Form onSubmit={handleSubmit}>
                            <Form.Group className="mb-3">
                                <Form.Label>User Type</Form.Label>
                                <Form.Select name="userType" value={userType} onChange={handleUserTypeChange}>
                                    <option value="user">User</option>
                                    <option value="lecturer">Lecturer</option>
                                </Form.Select>
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Full Name</Form.Label>
                                <Form.Control type="text" name="fullName" value={formData.fullName} onChange={handleChange} required />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Email</Form.Label>
                                <Form.Control type="email" name="email" value={formData.email} onChange={handleChange} required />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Role</Form.Label>
                                <Form.Select name="role" value={formData.role} onChange={handleChange}>
                                    {userType !== "lecturer" ? (
                                        <option value="USER">User</option>
                                    ) : (
                                        <option value="LECTURER">Lecturer</option>
                                    )}
                                    <option value="ADMINISTRATOR">Administrator</option>
                                </Form.Select>
                            </Form.Group>
                            {userType === "lecturer" && (
                                <>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Hours</Form.Label>
                                        <Form.Control type="number" name="hours" value={formData.hours} onChange={handleChange} required />
                                    </Form.Group>
                                </>
                            )}
                            <Button variant="primary" type="submit" className="w-100">
                                Update User
                            </Button>
                        </Form>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default UserEdit;
import React, { useState, useEffect } from "react";
import userServiceInstance from "../../../api/UserService";
import lecturerServiceInstance from "../../../api/LecturerService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";

const UserCreate = () => {

    const [userType, setUserType] = useState("user");
    const [formData, setFormData] = useState({
        fullName: "",
        email: "",
        role: "USER",
        hours: "",
        notes: "",
        seniority: "",
    });
    const [message, setMessage] = useState("");

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };

    const handleUserTypeChange = (event) => {
        const newUserType = event.target.value;
        setUserType(newUserType);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            if (userType === "lecturer") {
                await lecturerServiceInstance.insert({
                    fullName: formData.fullName,
                    email: formData.email,
                    role: formData.role,
                    hours: parseInt(formData.hours, 10),
                    notes: formData.notes,
                    seniority: formData.seniority
                });
            } else {
                await userServiceInstance.insert({
                    fullName: formData.fullName,
                    email: formData.email,
                    role: formData.role
                });
            }
            setMessage("User created successfully!");
            setFormData({
                fullName: "", email: "", role: "USER", hours: "", notes: "", seniority: "",
            });
        } catch (error) {
            setMessage("Error creating user. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Create User</h2>
                    {message && <Alert variant="success">{message}</Alert>}
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
                            <Form.Control
                                type="text"
                                name="fullName"
                                value={formData.fullName}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
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
                                    <Form.Control
                                        type="number"
                                        name="hours"
                                        value={formData.hours}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Seniority</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="seniority"
                                        value={formData.seniority}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Notes</Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        name="notes"
                                        value={formData.notes}
                                        onChange={handleChange}
                                        rows={3}
                                    />
                                </Form.Group>
                            </>
                        )}
                        <Button variant="primary" type="submit" className="w-100">
                            Create {userType.charAt(0).toUpperCase() + userType.slice(1)}
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default UserCreate;
import React, { useState } from "react";
import studyProgrammeServiceInstance from "../../../api/StudyProgrammeService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";

const StudyProgrammeCreate = () => {
    const [formData, setFormData] = useState({
        name: "",
        shortName: "",
        year: "",
    });

    const [message, setMessage] = useState("");

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({
            ...formData,
            [name]: type === "checkbox" ? checked : value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await studyProgrammeServiceInstance.insert({
                ...formData,
                year: parseInt(formData.year),
            });
            setMessage("Study Programme created successfully!");
            setFormData({ name: "", shortName: "", year: "" });
        } catch (error) {
            setMessage("Error creating Study Programme. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Create Study Programme</h2>
                    {message && <Alert variant="success">{message}</Alert>}
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Name</Form.Label>
                            <Form.Control
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Short Name</Form.Label>
                            <Form.Control
                                type="text"
                                name="shortName"
                                value={formData.shortName}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Year</Form.Label>
                            <Form.Control
                                type="number"
                                name="year"
                                value={formData.year}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Button variant="primary" type="submit" className="w-100">
                            Create Study Programme
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default StudyProgrammeCreate;
import React, { useState } from "react";
import classroomServiceInstance from "../../../api/ClassroomService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";

const ClassroomCreate = () => {
    const [formData, setFormData] = useState({
        building: "",
        number: "",
        seats: "",
        equipmentDescription: "",
    });

    const [message, setMessage] = useState("");

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await classroomServiceInstance.insert(formData);
            setMessage("Classroom created successfully!");
            setFormData({ building: "", number: "", seats: "",equipmentDescription: "" });
        } catch (error) {
            setMessage("Error creating classroom. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Create Classroom</h2>
                    {message && <Alert variant="success">{message}</Alert>}
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Building</Form.Label>
                            <Form.Control
                                type="text"
                                name="building"
                                value={formData.building}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Number</Form.Label>
                            <Form.Control
                                type="number"
                                name="number"
                                value={formData.number}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Seats</Form.Label>
                            <Form.Control
                                type="number"
                                name="seats"
                                value={formData.seats}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Equipment Description</Form.Label>
                            <Form.Control
                                as="textarea"
                                name="equipmentDescription"
                                value={formData.equipmentDescription}
                                onChange={handleChange}
                                rows={3}
                                required
                            />
                        </Form.Group>
                        <Button variant="primary" type="submit" className="w-100">
                            Create Classroom
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default ClassroomCreate;

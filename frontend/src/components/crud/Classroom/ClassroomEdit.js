import React, { useState, useEffect } from "react";
import classroomServiceInstance from "../../../api/ClassroomService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";
import { useParams } from "react-router-dom";

const ClassroomEdit = () => {
    const { id } = useParams();
    const [formData, setFormData] = useState({
        building: "",
        number: "",
        seats: "",
        equipmentDescription: "",
    });
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchClassroom = async () => {
            try {
                const response = await classroomServiceInstance.getById(id);
                setFormData({
                    building: response.data.building || "",
                    number: response.data.number || "",
                    seats: response.data.seats || "",
                    equipmentDescription: response.data.equipmentDescription || "",
                });
                setLoading(false);
            } catch (error) {
                setMessage("Error fetching classroom details.");
                setLoading(false);
            }
        };
        fetchClassroom();
    }, [id]);

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            await classroomServiceInstance.update(id, formData);
            setMessage("Classroom updated successfully!");
        } catch (error) {
            setMessage("Error updating classroom. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Edit Classroom</h2>
                    {message && <Alert variant={message.includes("Error") ? "danger" : "success"}>{message}</Alert>}
                    {loading ? (
                        <p className="text-center">Loading...</p>
                    ) : (
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
                                Update Classroom
                            </Button>
                        </Form>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default ClassroomEdit;
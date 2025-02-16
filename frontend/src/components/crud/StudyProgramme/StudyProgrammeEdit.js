import React, { useState, useEffect } from "react";
import studyProgrammeServiceInstance from "../../../api/StudyProgrammeService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";
import { useParams } from "react-router-dom";

const StudyProgrammeEdit = () => {
    const { id } = useParams();
    const [formData, setFormData] = useState({
        name: "",
        shortName: "",
        year: ""
    });
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStudyProgramme = async () => {
            try {
                const response = await studyProgrammeServiceInstance.getById(id);
                setFormData({
                    name: response.data.name || "",
                    shortName: response.data.shortName || "",
                    year: response.data.year || "",
                });
                setLoading(false);
            } catch (error) {
                setMessage("Error fetching study programme details.");
                setLoading(false);
            }
        };
        fetchStudyProgramme();
    }, [id]);

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
            await studyProgrammeServiceInstance.update(id, {
                ...formData,
            });
            setMessage("Study Programme updated successfully!");
        } catch (error) {
            setMessage("Error updating Study Programme. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Edit Study Programme</h2>
                    {message && <Alert variant={message.includes("Error") ? "danger" : "success"}>{message}</Alert>}
                    {loading ? (
                        <p className="text-center">Loading...</p>
                    ) : (
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
                                Update Study Programme
                            </Button>
                        </Form>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default StudyProgrammeEdit;
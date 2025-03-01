import React, { useState } from "react";
import Select from "react-select";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";
import semesterServiceInstance from "../../../api/SemesterService";

const SEMESTER_STATUSES = [
    { value: "PLANNED", label: "Planned" },
    { value: "ONGOING", label: "Ongoing" },
    { value: "ENDED", label: "Ended" }
];

const SemesterCreate = () => {
    const [formData, setFormData] = useState({
        name: "",
        semesterStatus: null,
    });
    const [message, setMessage] = useState("");

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };

    const handleSelectChange = (selectedOption) => {
        setFormData({ ...formData, semesterStatus: selectedOption });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const payload = {
            name: formData.name,
            semesterStatus: formData.semesterStatus?.value,
        };

        try {
            await semesterServiceInstance.insert(payload);
            setMessage("Semester created successfully!");
            setFormData({ name: "", semesterStatus: null });
        } catch (error) {
            console.error("Error creating semester:", error.response);
            setMessage("Error creating semester. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Create Semester</h2>
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
                            <Form.Label>Semester Status</Form.Label>
                            <Select 
                                options={SEMESTER_STATUSES} 
                                value={formData.semesterStatus} 
                                onChange={handleSelectChange} 
                                placeholder="Select semester status" 
                                required
                            />
                        </Form.Group>
                        <Button variant="primary" type="submit" className="w-100">
                            Create Semester
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default SemesterCreate;
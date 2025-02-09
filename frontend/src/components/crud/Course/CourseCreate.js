import React, { useState, useEffect } from "react";
import courseServiceInstance from "../../../api/CourseService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";
import studyProgrammeServiceInstance from "../../../api/StudyProgrammeService";

const CourseCreate = () => {
    const [formData, setFormData] = useState({
        name: "",
        description: "",
        studyProgramme: "",
    });
    const [studyProgrammes, setStudyProgrammes] = useState([]);
    const [message, setMessage] = useState("");

    useEffect(() => {
        const fetchStudyProgrammes = async () => {
            try {
                const response = await studyProgrammeServiceInstance.getAll();
                setStudyProgrammes(response.data);
            } catch (error) {
                console.error("Error fetching study programmes", error);
            }
        };
        fetchStudyProgrammes();
    }, []);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = {
            name: formData.name,
            description: formData.description,
            studyProgramme: { studyProgrammeId: formData.studyProgramme },
        };

        try {
            console.log("Submitting data:", payload);
            const response = await courseServiceInstance.insert(payload);
            setMessage("Course created successfully!");
            setFormData({ name: "", description: "", studyProgramme: "" });
        } catch (error) {
            console.error("Error creating course:", error.response);
            setMessage("Error creating course. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Create Course</h2>
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
                            <Form.Label>Description</Form.Label>
                            <Form.Control
                                as="textarea"
                                name="description"
                                value={formData.description}
                                onChange={handleChange}
                                rows={3}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Study Programme</Form.Label>
                            <Form.Select
                                name="studyProgramme"
                                value={formData.studyProgramme}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Select a Study Programme</option>
                                {studyProgrammes.map((programme) => (
                                    <option key={programme.studyProgrammeId} value={programme.studyProgrammeId}>
                                        {programme.name}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Button variant="primary" type="submit" className="w-100">
                            Create Course
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default CourseCreate;

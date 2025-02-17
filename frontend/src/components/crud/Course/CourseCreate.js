import React, { useState, useEffect } from "react";
import Select from "react-select";
import courseServiceInstance from "../../../api/CourseService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";
import studyProgrammeServiceInstance from "../../../api/StudyProgrammeService";

const CourseCreate = () => {
    const [formData, setFormData] = useState({
        name: "",
        description: "",
        studyProgrammes: [],
        creditPoints: "",
    });
    const [studyProgrammes, setStudyProgrammes] = useState([]);
    const [message, setMessage] = useState("");

    useEffect(() => {
        const fetchStudyProgrammes = async () => {
            try {
                const response = await studyProgrammeServiceInstance.getAll();
                setStudyProgrammes(response.data.map(studyProgramme => ({ value: studyProgramme.studyProgrammeId, label: `${studyProgramme.name} ${studyProgramme.year}` })));
            } catch (error) {
                console.error("Error fetching study programmes", error);
            }
        };
        fetchStudyProgrammes();
    }, []);

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };
    const handleSelectChange = (name) => (selectedOptions) => {
        setFormData({ ...formData, [name]: selectedOptions || [] });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const payload = {
            name: formData.name,
            description: formData.description,
            studyProgrammes: formData.studyProgrammes.map(studyProgramme => ({ studyProgrammeId: studyProgramme.value })),
            creditPoints: formData.creditPoints,
        };

        try {
            const response = await courseServiceInstance.insert(payload);
            setMessage("Course created successfully!");
            setFormData({ name: "", description: "", studyProgrammes: [], creditPoints: "" });
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
                            <Form.Label>CreditPoints</Form.Label>
                            <Form.Control
                                type="number"
                                name="creditPoints"
                                value={formData.creditPoints}
                                onChange={handleChange}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Study Programmes</Form.Label>
                            <Select isMulti options={studyProgrammes} value={formData.studyProgrammes} onChange={handleSelectChange("studyProgrammes")} placeholder="Select or search for study programmes" />
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

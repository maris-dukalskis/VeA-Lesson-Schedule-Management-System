import React, { useState, useEffect } from "react";
import courseServiceInstance from "../../../api/CourseService";
import studyProgrammeServiceInstance from "../../../api/StudyProgrammeService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";
import { useParams } from "react-router-dom";

const CourseEdit = () => {
    const { id } = useParams();
    const [formData, setFormData] = useState({
        name: "",
        description: "",
        studyProgramme: "",
    });
    const [studyProgrammes, setStudyProgrammes] = useState([]);
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchCourse = async () => {
            try {
                const response = await courseServiceInstance.getById(id);
                setFormData({
                    name: response.data.name || "",
                    description: response.data.description || "",
                    studyProgramme: response.data.studyProgramme?.studyProgrammeId || "",
                });
                setLoading(false);
            } catch (error) {
                setMessage("Error fetching course details.");
                setLoading(false);
            }
        };
        
        const fetchStudyProgrammes = async () => {
            try {
                const response = await studyProgrammeServiceInstance.getAll();
                setStudyProgrammes(response.data);
            } catch (error) {
                console.error("Error fetching study programmes", error);
            }
        };
        
        fetchCourse();
        fetchStudyProgrammes();
    }, [id]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const payload = {
                name: formData.name,
                description: formData.description,
                studyProgramme: { studyProgrammeId: formData.studyProgramme },
            };
            await courseServiceInstance.update(id, payload);
            setMessage("Course updated successfully!");
        } catch (error) {
            setMessage("Error updating course. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Edit Course</h2>
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
                                Update Course
                            </Button>
                        </Form>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default CourseEdit;
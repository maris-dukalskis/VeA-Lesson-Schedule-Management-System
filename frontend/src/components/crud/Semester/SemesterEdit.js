import React, { useState, useEffect } from "react";
import Select from "react-select";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";
import { useParams } from "react-router-dom";
import semesterServiceInstance from "../../../api/SemesterService";

const SEMESTER_STATUSES = [
    { value: "PLANNED", label: "Planned" },
    { value: "ONGOING", label: "Ongoing" },
    { value: "ENDED", label: "Ended" }
];

const SemesterEdit = () => {
    const { id } = useParams();
    const [formData, setFormData] = useState({
        name: "",
        semesterStatus: null,
    });
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchSemester = async () => {
            try {
                const response = await semesterServiceInstance.getById(id);
                setFormData({
                    name: response.data.name || "",
                    semesterStatus: SEMESTER_STATUSES.find(status => status.value === response.data.semesterStatus) || null,
                });
                setLoading(false);
            } catch (error) {
                setMessage("Error fetching semester details.");
                setLoading(false);
            }
        };

        fetchSemester();
    }, [id]);

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };

    const handleSelectChange = (selectedOption) => {
        setFormData({ ...formData, semesterStatus: selectedOption });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const payload = {
                name: formData.name,
                semesterStatus: formData.semesterStatus?.value,
            };

            await semesterServiceInstance.update(id, payload);
            setMessage("Semester updated successfully!");
        } catch (error) {
            setMessage("Error updating semester. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Edit Semester</h2>
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
                                Update Semester
                            </Button>
                        </Form>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default SemesterEdit;
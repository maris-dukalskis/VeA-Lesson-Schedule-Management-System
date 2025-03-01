import React, { useState, useEffect } from "react";
import Select from "react-select";
import courseServiceInstance from "../../../api/CourseService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";
import studyProgrammeServiceInstance from "../../../api/StudyProgrammeService";
import courseStudyProgrammeAliasServiceInstance from "../../../api/CourseStudyProgrammeAliasService";
import { Row, Col } from "react-bootstrap";

const CourseCreate = () => {
    const [formData, setFormData] = useState({
        name: "",
        shortName: "",
        description: "",
        studyProgrammes: [],
        creditPoints: "",
    });
    const [studyProgrammes, setStudyProgrammes] = useState([]);
    const [studyProgrammeAliases, setStudyProgrammeAliases] = useState([]);
    const [message, setMessage] = useState("");

    useEffect(() => {
        const fetchStudyProgrammes = async () => {
            try {
                const response = await studyProgrammeServiceInstance.getAll();
                setStudyProgrammes(response.data.map(studyProgramme => ({ 
                    value: studyProgramme.studyProgrammeId, 
                    label: `${studyProgramme.name} ${studyProgramme.year}` 
                })));
            } catch (error) {
                console.error("Error fetching study programmes", error);
            }
        };
        fetchStudyProgrammes();
    }, []);

    useEffect(() => {
        updateStudyProgrammeAliases();
    }, [formData.studyProgrammes]);

    const updateStudyProgrammeAliases = () => {
        const existingEntries = studyProgrammeAliases.filter(studyProgramme =>
            formData.studyProgrammes.some(selected => selected.value === studyProgramme.value)
        );

        const newEntries = formData.studyProgrammes
            .filter(studyProgramme => !studyProgrammeAliases.some(existing => existing.value === studyProgramme.value))
            .map(studyProgramme => ({
                ...studyProgramme,
                alias: ""
            }));

        setStudyProgrammeAliases([...existingEntries, ...newEntries]);
    };

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };

    const handleSelectChange = (selectedOptions) => {
        setFormData({ ...formData, studyProgrammes: selectedOptions || [] });
    };

    const handleAliasChange = (index, value) => {
        const updatedAliases = studyProgrammeAliases.map((item, i) => 
            i === index ? { ...item, alias: value } : { ...item }
        );
        setStudyProgrammeAliases(updatedAliases);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            const courseResponse = await courseServiceInstance.insert({
                name: formData.name,
                shortName: formData.shortName,
                description: formData.description,
                creditPoints: formData.creditPoints,
            });

            const courseId = courseResponse.data.courseId;

            await Promise.all(formData.studyProgrammes.map(studyProgramme => {
                const aliasEntry = studyProgrammeAliases.find(entry => entry.value === studyProgramme.value);
                return courseStudyProgrammeAliasServiceInstance.insert({
                    course: { courseId: courseId },
                    studyProgramme: { studyProgrammeId: studyProgramme.value },
                    alias: aliasEntry?.alias || ""
                });
            }));

            setMessage("Course created successfully!");
            setFormData({ 
                name: "", 
                shortName: "", 
                description: "", 
                studyProgrammes: [], 
                creditPoints: "" 
            });
            setStudyProgrammeAliases([]);
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
                            <Form.Label>Credit Points</Form.Label>
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
                            <Select 
                                isMulti 
                                options={studyProgrammes} 
                                value={formData.studyProgrammes} 
                                onChange={handleSelectChange} 
                                placeholder="Select or search for study programmes" 
                            />
                        </Form.Group>

                        {studyProgrammeAliases.length > 0 && (
                            <div className="mt-4 mb-3">
                                <h5>Study Programme Aliases</h5>
                                {studyProgrammeAliases.map((studyProgramme, index) => (
                                    <Row key={index} className="align-items-center mt-2">
                                        <Col>
                                            <Form.Control
                                                type="text"
                                                value={studyProgramme.label}
                                                disabled
                                            />
                                        </Col>
                                        <Col>
                                            <Form.Control
                                                type="text"
                                                value={studyProgramme.alias || ""}
                                                onChange={(e) => handleAliasChange(index, e.target.value)}
                                                placeholder="Enter Alias"
                                            />
                                        </Col>
                                    </Row>
                                ))}
                            </div>
                        )}

                        <div className="mb-3"></div>
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
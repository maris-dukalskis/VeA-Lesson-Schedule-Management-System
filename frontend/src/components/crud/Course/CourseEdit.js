import React, { useState, useEffect } from "react";
import Select from "react-select";
import courseServiceInstance from "../../../api/CourseService";
import studyProgrammeServiceInstance from "../../../api/StudyProgrammeService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";
import { useParams } from "react-router-dom";
import courseStudyProgrammeAliasServiceInstance from "../../../api/CourseStudyProgrammeAliasService";
import { Row, Col } from "react-bootstrap";

const CourseEdit = () => {
    const { id } = useParams();
    const [formData, setFormData] = useState({
        name: "",
        shortName: "",
        description: "",
        studyProgrammes: [],
        creditPoints: "",
    });
    const [dbStudyProgrammes, setDbStudyProgrammes] = useState([]);
    const [studyProgrammeAliases, setStudyProgrammeAliases] = useState([]);
    const [studyProgrammes, setStudyProgrammes] = useState([]);
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const courseResponse = await courseServiceInstance.getById(id);
                const studyProgrammeResponse = await studyProgrammeServiceInstance.getAll();
                const courseStudyProgrammeAliasResponse = await courseStudyProgrammeAliasServiceInstance.getAllByCourseId(id);

                setStudyProgrammes(studyProgrammeResponse.data.map(studyProgramme => ({ value: studyProgramme.studyProgrammeId, label: `${studyProgramme.name} ${studyProgramme.year}` })));

                const currentStudyProgrammes = courseStudyProgrammeAliasResponse.data.map(alias => ({
                    value: alias.studyProgramme.studyProgrammeId,
                    label: `${alias.studyProgramme.name} ${alias.studyProgramme.year}`,
                    alias: alias.alias,
                    courseStudyProgrammeAliasId: alias.courseStudyProgrammeAliasId
                }));

                setDbStudyProgrammes(JSON.parse(JSON.stringify(currentStudyProgrammes)));
                setStudyProgrammeAliases(JSON.parse(JSON.stringify(currentStudyProgrammes)));

                setFormData({
                    name: courseResponse.data.name || "",
                    shortName: courseResponse.data.shortName || "",
                    description: courseResponse.data.description || "",
                    creditPoints: courseResponse.data.creditPoints || "",
                    studyProgrammes: currentStudyProgrammes || [],
                });

                setLoading(false);
            } catch (error) {
                setMessage("Error fetching details.");
                setLoading(false);
            }
        };
        fetchData();
    }, [id]);

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
                alias: "",
                courseStudyProgrammeAliasId: null
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
        const updatedAliases = [...studyProgrammeAliases];
        updatedAliases[index].alias = value;
        setStudyProgrammeAliases(updatedAliases);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            await courseServiceInstance.update(id, {
                name: formData.name,
                shortName: formData.shortName,
                description: formData.description,
                creditPoints: formData.creditPoints,
            });

            const currentIds = formData.studyProgrammes.map(studyProgramme => studyProgramme.value);
            const toRemove = dbStudyProgrammes.filter(
                studyProgramme => !currentIds.includes(studyProgramme.value)
            );

            await Promise.all(toRemove.map(studyProgramme =>
                courseStudyProgrammeAliasServiceInstance.delete(studyProgramme.courseStudyProgrammeAliasId)
            ));

            await Promise.all(formData.studyProgrammes.map(studyProgramme => {
                const aliasEntry = studyProgrammeAliases.find(entry => entry.value === studyProgramme.value);

                const dbEntry = dbStudyProgrammes.find(entry => entry.value === studyProgramme.value);

                if (dbEntry) {
                    if (aliasEntry.alias !== dbEntry.alias) {
                        return courseStudyProgrammeAliasServiceInstance.update(
                            dbEntry.courseStudyProgrammeAliasId,
                            {
                                course: { courseId: id },
                                studyProgramme: { studyProgrammeId: studyProgramme.value },
                                alias: aliasEntry.alias
                            }
                        );
                    }
                    return Promise.resolve();
                } else {
                    return courseStudyProgrammeAliasServiceInstance.insert({
                        course: { courseId: id },
                        studyProgramme: { studyProgrammeId: studyProgramme.value },
                        alias: aliasEntry?.alias || ""
                    });
                }
            }));
            setMessage("Course updated successfully!");
        } catch (error) {
            console.error("Error updating course:", error.response);
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
                                <Select isMulti options={studyProgrammes} value={formData.studyProgrammes} onChange={handleSelectChange} placeholder="Select or search for study programmes" />
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
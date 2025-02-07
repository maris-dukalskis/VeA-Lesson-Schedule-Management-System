import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import userServiceInstance from "../../../api/UserService";
import studentServiceInstance from "../../../api/StudentService";
import lecturerServiceInstance from "../../../api/LecturerService";
import studyProgrammeServiceInstance from "../../../api/StudyProgrammeService";
import { Container, Card, Form, Button, Alert } from "react-bootstrap";

const UserEdit = () => {
    const { id } = useParams();
    const [userType, setUserType] = useState("user");
    const [studyProgrammes, setStudyProgrammes] = useState([]);
    const [formData, setFormData] = useState({
        fullName: "",
        email: "",
        matriculeNumber: "",
        studyProgrammeId: "",
        role: "EDITOR",
        hours: ""
    });
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await userServiceInstance.getById(id);
                setFormData(prevData => ({
                    ...prevData,
                    fullName: response.data.fullName || "",
                    email: response.data.email || "",
                    matriculeNumber: response.data.matriculeNumber || "",
                    studyProgrammeId: response.data.studyProgramme?.studyProgrammeId || "",
                    role: response.data.role || "EDITOR",
                    hours: response.data.hours || ""
                }));
                setUserType(response.data.matriculeNumber ? "student" : response.data.hours ? "lecturer" : "user");
            } catch (error) {
                setMessage("Error fetching user details.");
            } finally {
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

        fetchUserData();
        fetchStudyProgrammes();
    }, [id]);

    const handleChange = (e) => {
        setFormData(prevData => ({ ...prevData, [e.target.name]: e.target.value }));
    };

    const handleUserTypeChange = (e) => {
        const newUserType = e.target.value;
        setUserType(newUserType);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            console.log(formData);
            if (userType === "student") {
                await studentServiceInstance.update(id, {
                    fullName: formData.fullName,
                    email: formData.email,
                    matriculeNumber: parseInt(formData.matriculeNumber, 10),
                    studyProgramme: { studyProgrammeId: formData.studyProgrammeId }
                });
            } else if (userType === "lecturer") {
                await lecturerServiceInstance.update(id, {
                    fullName: formData.fullName,
                    email: formData.email,
                    role: formData.role,
                    hours: parseInt(formData.hours, 10)
                });
            } else {
                await userServiceInstance.update(id, {
                    fullName: formData.fullName,
                    email: formData.email,
                    role: formData.role
                });
            }
            setMessage("User updated successfully!");
        } catch (error) {
            setMessage("Error updating user. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Edit User</h2>
                    {message && <Alert variant={message.includes("Error") ? "danger" : "success"}>{message}</Alert>}
                    {loading ? (
                        <p className="text-center">Loading...</p>
                    ) : (
                        <Form onSubmit={handleSubmit}>
                            <Form.Group className="mb-3">
                                <Form.Label>User Type</Form.Label>
                                <Form.Select name="userType" value={userType} onChange={handleUserTypeChange}>
                                    <option value="user">User</option>
                                    <option value="student">Student</option>
                                    <option value="lecturer">Lecturer</option>
                                </Form.Select>
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Full Name</Form.Label>
                                <Form.Control type="text" name="fullName" value={formData.fullName} onChange={handleChange} required />
                            </Form.Group>
                            <Form.Group className="mb-3">
                                <Form.Label>Email</Form.Label>
                                <Form.Control type="email" name="email" value={formData.email} onChange={handleChange} required />
                            </Form.Group>
                            {userType === "student" && (
                                <>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Matricule Number</Form.Label>
                                        <Form.Control type="number" name="matriculeNumber" value={formData.matriculeNumber} onChange={handleChange} required />
                                    </Form.Group>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Study Programme</Form.Label>
                                        <Form.Select name="studyProgrammeId" value={formData.studyProgrammeId} onChange={handleChange} required>
                                            <option value="">Select a Study Programme</option>
                                            {studyProgrammes.map((programme) => (
                                                <option key={programme.studyProgrammeId} value={programme.studyProgrammeId}>
                                                    {programme.name}
                                                </option>
                                            ))}
                                        </Form.Select>
                                    </Form.Group>
                                </>
                            )}
                            {userType !== "student" && (
                                <Form.Group className="mb-3">
                                    <Form.Label>Role</Form.Label>
                                    <Form.Select name="role" value={formData.role} onChange={handleChange}>
                                        <option value="EDITOR">Editor</option>
                                        <option value="ADMINISTRATOR">Administrator</option>
                                    </Form.Select>
                                </Form.Group>
                            )}
                            {userType === "lecturer" && (
                                <>
                                    <Form.Group className="mb-3">
                                        <Form.Label>Hours</Form.Label>
                                        <Form.Control type="number" name="hours" value={formData.hours} onChange={handleChange} required />
                                    </Form.Group>
                                </>
                            )}
                            <Button variant="primary" type="submit" className="w-100">
                                Update User
                            </Button>
                        </Form>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default UserEdit;
import React, { useState, useEffect } from "react";
import lessonServiceInstance from "../../../api/LessonService";
import { Container, Table, Button, Modal, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const LessonList = () => {
    const [lessons, setLessons] = useState([]);
    const [selectedLesson, setSelectedLesson] = useState(null);
    const [showViewModal, setShowViewModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        fetchLessons();
    }, []);

    const fetchLessons = async () => {
        try {
            const response = await lessonServiceInstance.getAll();
            setLessons(response.data);
        } catch (error) {
            console.error("Error fetching lessons", error);
        }
    };

    const handleView = (lesson) => {
        setSelectedLesson(lesson);
        setShowViewModal(true);
    };

    const handleEdit = (id) => {
        navigate(`/lesson/edit/${id}`);
    };

    const handleDelete = async () => {
        try {
            await lessonServiceInstance.delete(deleteId);
            setMessage("Lesson deleted successfully!");
            setShowDeleteModal(false);
            fetchLessons();
        } catch (error) {
            setMessage("Error deleting lesson. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <h2 className="text-center mb-4">Lesson List</h2>
            {message && <Alert variant="success">{message}</Alert>}
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>Course</th>
                        <th>Classroom</th>
                        <th>Lecturer</th>
                        <th>Group</th>
                        <th>Online</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {lessons.map((lesson) => (
                        <tr key={lesson.lessonId}>
                            <td>{lesson.course.name}</td>
                            <td>{lesson.classroom?.building + lesson.classroom?.number}</td>
                            <td>{lesson.lecturer?.fullName}</td>
                            <td>{lesson.lessonGroup || "N/A"}</td>
                            <td>{lesson.online ? "Yes" : "No"}</td>
                            <td>
                                <Button variant="info" className="me-2" onClick={() => handleView(lesson)}>
                                    View
                                </Button>
                                <Button variant="warning" className="me-2" onClick={() => handleEdit(lesson.lessonId)}>
                                    Edit
                                </Button>
                                <Button
                                    variant="danger"
                                    onClick={() => {
                                        setDeleteId(lesson.lessonId);
                                        setShowDeleteModal(true);
                                    }}
                                >
                                    Delete
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            <Modal show={showViewModal} onHide={() => setShowViewModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Lesson Details</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedLesson && (
                        <>
                            <p><strong>Course:</strong> {selectedLesson.course?.name}</p>
                            <p><strong>Classroom:</strong> {selectedLesson.classroom?.building + selectedLesson.classroom?.number}</p>
                            <p><strong>Lecturer:</strong> {selectedLesson.lecturer?.fullName}</p>
                            <p><strong>Online:</strong> {selectedLesson.online ? "Yes" : "No"}</p>
                            {selectedLesson.online && <p><strong>Online Information:</strong> {selectedLesson.onlineInformation}</p>}
                            <p><strong>Lesson Group:</strong> {selectedLesson.lessonGroup || "N/A"}</p>
                        </>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowViewModal(false)}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>

            <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Confirm Deletion</Modal.Title>
                </Modal.Header>
                <Modal.Body>Are you sure you want to delete this lesson?</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleDelete}>
                        Delete
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
};

export default LessonList;
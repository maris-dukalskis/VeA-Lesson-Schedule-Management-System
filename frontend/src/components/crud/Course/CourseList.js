import React, { useState, useEffect } from "react";
import courseServiceInstance from "../../../api/CourseService";
import { Container, Table, Button, Modal, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const CourseList = () => {
    const [courses, setCourses] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [showViewModal, setShowViewModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        fetchCourses();
    }, []);

    const fetchCourses = async () => {
        try {
            const response = await courseServiceInstance.getAll();
            setCourses(response.data);
        } catch (error) {
            console.error("Error fetching courses", error);
        }
    };

    const handleView = (course) => {
        setSelectedCourse(course);
        setShowViewModal(true);
    };

    const handleEdit = (id) => {
        navigate(`/course/edit/${id}`);
    };

    const handleDelete = async () => {
        try {
            await courseServiceInstance.delete(deleteId);
            setMessage("Course deleted successfully!");
            setShowDeleteModal(false);
            fetchCourses();
        } catch (error) {
            setMessage("Error deleting course. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <h2 className="text-center mb-4">Course List</h2>
            {message && <Alert variant="success">{message}</Alert>}
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>CreditPoints</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {courses.map((course) => (
                        <tr key={course.courseId}>
                            <td>{course.name}</td>
                            <td>{course.description}</td>
                            <td>{course.creditPoints}</td>
                            <td>
                                <Button
                                    variant="info"
                                    className="me-2"
                                    onClick={() => handleView(course)}
                                >
                                    View
                                </Button>
                                <Button
                                    variant="warning"
                                    className="me-2"
                                    onClick={() => handleEdit(course.courseId)}
                                >
                                    Edit
                                </Button>
                                <Button
                                    variant="danger"
                                    onClick={() => {
                                        setDeleteId(course.courseId);
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
                    <Modal.Title>Course Details</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedCourse && (
                        <>
                            <p><strong>Name:</strong> {selectedCourse.name}</p>
                            <p><strong>Description:</strong> {selectedCourse.description}</p>
                            <p><strong>CreditPoints:</strong> {selectedCourse.creditPoints}</p>
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
                <Modal.Body>Are you sure you want to delete this course?</Modal.Body>
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

export default CourseList;
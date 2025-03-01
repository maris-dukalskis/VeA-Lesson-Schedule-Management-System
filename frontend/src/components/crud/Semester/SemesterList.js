import React, { useState, useEffect } from "react";
import semesterServiceInstance from "../../../api/SemesterService";
import { Container, Table, Button, Modal, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const SemesterList = () => {
    const [semesters, setSemesters] = useState([]);
    const [selectedSemester, setSelectedSemester] = useState(null);
    const [showViewModal, setShowViewModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        fetchSemesters();
    }, []);

    const fetchSemesters = async () => {
        try {
            const response = await semesterServiceInstance.getAll();
            setSemesters(response.data);
        } catch (error) {
            console.error("Error fetching semesters", error);
        }
    };

    const handleView = (semester) => {
        setSelectedSemester(semester);
        setShowViewModal(true);
    };

    const handleEdit = (id) => {
        navigate(`/semester/edit/${id}`);
    };

    const handleDelete = async () => {
        try {
            await semesterServiceInstance.delete(deleteId);
            setMessage("Semester deleted successfully!");
            setShowDeleteModal(false);
            fetchSemesters();
        } catch (error) {
            setMessage("Error deleting semester. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <h2 className="text-center mb-4">Semester List</h2>
            {message && <Alert variant="success">{message}</Alert>}
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {semesters.map((semester) => (
                        <tr key={semester.semesterId}>
                            <td>{semester.name}</td>
                            <td>{semester.semesterStatus}</td>
                            <td>
                                <Button
                                    variant="info"
                                    className="me-2"
                                    onClick={() => handleView(semester)}
                                >
                                    View
                                </Button>
                                <Button
                                    variant="warning"
                                    className="me-2"
                                    onClick={() => handleEdit(semester.semesterId)}
                                >
                                    Edit
                                </Button>
                                <Button
                                    variant="danger"
                                    onClick={() => {
                                        setDeleteId(semester.semesterId);
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
                    <Modal.Title>Semester Details</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedSemester && (
                        <>
                            <p><strong>Name:</strong> {selectedSemester.name}</p>
                            <p><strong>Status:</strong> {selectedSemester.semesterStatus}</p>
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
                <Modal.Body>Are you sure you want to delete this semester?</Modal.Body>
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

export default SemesterList;
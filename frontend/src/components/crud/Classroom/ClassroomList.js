import React, { useState, useEffect } from "react";
import classroomServiceInstance from "../../../api/ClassroomService";
import { Container, Table, Button, Modal, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const ClassroomList = () => {
    const [classrooms, setClassrooms] = useState([]);
    const [selectedClassroom, setSelectedClassroom] = useState(null);
    const [showViewModal, setShowViewModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        fetchClassrooms();
    }, []);

    const fetchClassrooms = async () => {
        try {
            const response = await classroomServiceInstance.getAll();
            setClassrooms(response.data);
        } catch (error) {
            console.error("Error fetching classrooms", error);
        }
    };

    const handleView = (classroom) => {
        setSelectedClassroom(classroom);
        setShowViewModal(true);
    };

    const handleEdit = (id) => {
        navigate(`/classroom/edit/${id}`);
    };

    const handleDelete = async () => {
        try {
            await classroomServiceInstance.delete(deleteId);
            setMessage("Classroom deleted successfully!");
            setShowDeleteModal(false);
            fetchClassrooms();
        } catch (error) {
            setMessage("Error deleting classroom. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <h2 className="text-center mb-4">Classroom List</h2>
            {message && <Alert variant="success">{message}</Alert>}
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>Building</th>
                        <th>Number</th>
                        <th>Seats</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {classrooms.map((classroom) => (
                        <tr key={classroom.classroomId}>
                            <td>{classroom.building}</td>
                            <td>{classroom.number}</td>
                            <td>{classroom.seats}</td>
                            <td>
                                <Button
                                    variant="info"
                                    className="me-2"
                                    onClick={() => handleView(classroom)}
                                >
                                    View
                                </Button>
                                <Button
                                    variant="warning"
                                    className="me-2"
                                    onClick={() => handleEdit(classroom.classroomId)}
                                >
                                    Edit
                                </Button>
                                <Button
                                    variant="danger"
                                    onClick={() => {
                                        setDeleteId(classroom.classroomId);
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
                    <Modal.Title>Classroom Details</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedClassroom && (
                        <>
                            <p><strong>Building:</strong> {selectedClassroom.building}</p>
                            <p><strong>Number:</strong> {selectedClassroom.number}</p>
                            <p><strong>Seats:</strong> {selectedClassroom.seats}</p>
                            <p><strong>Equipment Description:</strong> {selectedClassroom.equipmentDescription}</p>
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
                <Modal.Body>Are you sure you want to delete this classroom?</Modal.Body>
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

export default ClassroomList;

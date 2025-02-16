import React, { useState, useEffect } from "react";
import studyProgrammeServiceInstance from "../../../api/StudyProgrammeService";
import { Container, Table, Button, Modal, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const StudyProgrammeList = () => {
    const [studyProgrammes, setStudyProgrammes] = useState([]);
    const [selectedProgramme, setSelectedProgramme] = useState(null);
    const [showViewModal, setShowViewModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        fetchStudyProgrammes();
    }, []);

    const fetchStudyProgrammes = async () => {
        try {
            const response = await studyProgrammeServiceInstance.getAll();
            setStudyProgrammes(response.data);
        } catch (error) {
            console.error("Error fetching study programmes", error);
        }
    };

    const handleView = (programme) => {
        setSelectedProgramme(programme);
        setShowViewModal(true);
    };

    const handleEdit = (id) => {
        navigate(`/studyprogramme/edit/${id}`);
    };

    const handleDelete = async () => {
        try {
            await studyProgrammeServiceInstance.delete(deleteId);
            setMessage("Study Programme deleted successfully!");
            setShowDeleteModal(false);
            fetchStudyProgrammes();
        } catch (error) {
            setMessage("Error deleting study programme. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <h2 className="text-center mb-4">Study Programme List</h2>
            {message && <Alert variant="success">{message}</Alert>}
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Short Name</th>
                        <th>Year</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {studyProgrammes.map((programme) => (
                        <tr key={programme.studyProgrammeId}>
                            <td>{programme.name}</td>
                            <td>{programme.shortName}</td>
                            <td>{programme.year}</td>
                            <td>
                                <Button
                                    variant="info"
                                    className="me-2"
                                    onClick={() => handleView(programme)}
                                >
                                    View
                                </Button>
                                <Button
                                    variant="warning"
                                    className="me-2"
                                    onClick={() => handleEdit(programme.studyProgrammeId)}
                                >
                                    Edit
                                </Button>
                                <Button
                                    variant="danger"
                                    onClick={() => {
                                        setDeleteId(programme.studyProgrammeId);
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
                    <Modal.Title>Study Programme Details</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedProgramme && (
                        <>
                            <p><strong>Name:</strong> {selectedProgramme.name}</p>
                            <p><strong>Short Name:</strong> {selectedProgramme.shortName}</p>
                            <p><strong>Year:</strong> {selectedProgramme.year}</p>
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
                <Modal.Body>Are you sure you want to delete this study programme?</Modal.Body>
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

export default StudyProgrammeList;
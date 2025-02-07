import React, { useState, useEffect } from "react";
import userServiceInstance from "../../../api/UserService";
import { Container, Table, Button, Modal, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const UserList = () => {
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [showViewModal, setShowViewModal] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [message, setMessage] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const response = await userServiceInstance.getAll();
            setUsers(response.data);
        } catch (error) {
            console.error("Error fetching users", error);
        }
    };

    const handleView = (user) => {
        setSelectedUser(user);
        setShowViewModal(true);
    };

    const handleEdit = (id) => {
        navigate(`/user/edit/${id}`);
    };

    const handleDelete = async () => {
        try {
            await userServiceInstance.delete(deleteId);
            setMessage("User deleted successfully!");
            setShowDeleteModal(false);
            fetchUsers();
        } catch (error) {
            setMessage("Error deleting user. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <h2 className="text-center mb-4">User List</h2>
            {message && <Alert variant="success">{message}</Alert>}
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map((user) => (
                        <tr key={user.userId}>
                            <td>{user.fullName}</td>
                            <td>{user.email}</td>
                            <td>
                                <Button variant="info" className="me-2" onClick={() => handleView(user)}>
                                    View
                                </Button>
                                <Button variant="warning" className="me-2" onClick={() => handleEdit(user.userId)}>
                                    Edit
                                </Button>
                                <Button variant="danger" onClick={() => {
                                    setDeleteId(user.userId);
                                    setShowDeleteModal(true);
                                }}>
                                    Delete
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            <Modal show={showViewModal} onHide={() => setShowViewModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>User Details</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedUser && (
                        <>
                            <p><strong>Full Name:</strong> {selectedUser.fullName}</p>
                            <p><strong>Email:</strong> {selectedUser.email}</p>
                            <p><strong>Role:</strong> {selectedUser.role}</p>
                            <p><strong>Type:</strong> {selectedUser.dtype}</p>
                            {selectedUser.dtype === "Student" && (
                                <>
                                    <p><strong>Matricule Number:</strong> {selectedUser.matriculeNumber}</p>
                                    <p><strong>Study Programme:</strong> {selectedUser.studyProgramme?.name}</p>
                                </>
                            )}
                            {selectedUser.dtype === "Lecturer" && (
                                <>
                                    <p><strong>Hours:</strong> {selectedUser.hours}</p>
                                </>
                            )}
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
                <Modal.Body>Are you sure you want to delete this user?</Modal.Body>
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

export default UserList;
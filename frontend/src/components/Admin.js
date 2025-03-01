import React from "react";
import { Container, Card, Button, Row, Col } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const components = [
  { name: "Classroom", color: "primary" },
  { name: "Course", color: "success" },
  { name: "Lesson", color: "warning" },
  { name: "StudyProgramme", color: "info" },
  { name: "User", color: "danger" },
  { name: "Semester", color: "primary" },
];

const AdminPanel = () => {
  const navigate = useNavigate();

  return (
    <Container className="py-4">
      <h1 className="mb-4">Admin Panel</h1>
      <Row className="g-4">
        {components.map(({ name, color }) => (
          <Col key={name} xs={12} md={6}>
            <Card className={`p-3 shadow-sm border-${color}`}>
              <Card.Body>
                <Card.Title className={`text-${color}`}>{name}</Card.Title>
                <Card.Text>
                  Some information about the {name.toLowerCase()} component
                </Card.Text>
                <div className="d-flex gap-3">
                  <Button 
                    variant={color} 
                    onClick={() => navigate(`/${name.toLowerCase()}/list`)}
                  >
                    List
                  </Button>
                  <Button 
                    variant={`outline-${color}`} 
                    onClick={() => navigate(`/${name.toLowerCase()}/create`)}
                  >
                    Create
                  </Button>
                </div>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
};

export default AdminPanel;
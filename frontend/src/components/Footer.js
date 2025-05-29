import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';

const Footer = () => {
    const schoolAddress = "Inženieru 101, LV-3601, Ventspils, Latvija";
    const schoolPhone = "636 296 57";
    const schoolEmail = "venta@venta.lv";
    const currentYear = new Date().getFullYear();
    const schoolName = "Ventspils Augstskola";

    return (
        <footer className="bg-light text-dark py-4 mt-5 border-top">
            <Container>
                <Row className="justify-content-center text-center">
                    <Col md={4} className="mb-3 mb-md-0">
                        <h5>Adrese</h5>
                        <p className="mb-0">{schoolAddress}</p>
                    </Col>
                    <Col md={4} className="mb-3 mb-md-0">
                        <h5>Telefons</h5>
                        <p className="mb-0">{schoolPhone}</p>
                    </Col>
                    <Col md={4} className="mb-3 mb-md-0">
                        <h5>Epasts</h5>
                        <p className="mb-0"><a href={`mailto:${schoolEmail}`} className="text-dark">{schoolEmail}</a></p>
                    </Col>
                    <Col md={4}>
                        <h5>&nbsp;</h5>
                        <p className="mb-0">
                            &copy; {currentYear} {schoolName}. Visas tiesības aizsargātas.
                        </p>
                    </Col>
                </Row>
            </Container>
        </footer>
    );
};

export default Footer;
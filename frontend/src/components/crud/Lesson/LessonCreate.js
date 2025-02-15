import React, { useState, useEffect } from "react";
import lessonServiceInstance from "../../../api/LessonService";
import lessonDateTimeServiceInstance from "../../../api/LessonDateTimeService";
import courseServiceInstance from "../../../api/CourseService";
import classroomServiceInstance from "../../../api/ClassroomService";
import lecturerServiceInstance from "../../../api/LecturerService";
import { Container, Card, Form, Button, Alert, Row, Col } from "react-bootstrap";
import Select from "react-select";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { X } from "lucide-react";

const defaultTimes = [
    { value: { from: "09:00", to: "10:30" }, label: "09:00-10:30" },
    { value: { from: "10:40", to: "12:10" }, label: "10:40-12:10" },
    { value: { from: "13:00", to: "14:30" }, label: "13:00-14:30" },
    { value: { from: "14:40", to: "16:10" }, label: "14:40-16:10" },
    { value: { from: "16:15", to: "17:45" }, label: "16:15-17:45" },
    { value: { from: "17:50", to: "19:20" }, label: "17:50-19:20" },
    { value: { from: "19:25", to: "20:55" }, label: "19:25-20:55" }
];

const LessonCreate = () => {
    const [formData, setFormData] = useState({
        course: "",
        classroom: null,
        lecturer: null,
        datesTimes: [],
        online: false,
        onlineInformation: "",
        lessonGroup: "",
    });
    const [courses, setCourses] = useState([]);
    const [classrooms, setClassrooms] = useState([]);
    const [lecturers, setLecturers] = useState([]);
    const [message, setMessage] = useState("");
    const [customTimesEnabled, setCustomTimesEnabled] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [courseRes, classroomRes, lecturerRes] = await Promise.all([
                    courseServiceInstance.getAll(),
                    classroomServiceInstance.getAll(),
                    lecturerServiceInstance.getAll()
                ]);
                setCourses(courseRes.data);
                setClassrooms(classroomRes.data.map(c => ({ value: c.classroomId, label: `${c.building}${c.number}` })));
                setLecturers(lecturerRes.data.map(l => ({ value: l.userId, label: l.fullName })));
            } catch (error) {
                console.error("Error fetching data", error);
            }
        };
        fetchData();
    }, []);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({
            ...formData,
            [name]: type === "checkbox" ? checked : value,
        });
    };

    const handleSelectChange = (name) => (selectedOptions) => {
        setFormData({ ...formData, [name]: selectedOptions || [] });
    };

    const handleDateChange = (date, index) => {
        const updatedDatesTimes = [...formData.datesTimes];
        if (date) {
            date.setHours(12, 0, 0, 0);
        }

        updatedDatesTimes[index] = {
            ...updatedDatesTimes[index],
            date: date ? date.toISOString().split('T')[0] : "",
            times: updatedDatesTimes[index]?.times || []
        };
        setFormData({ ...formData, datesTimes: updatedDatesTimes });
    };

    const handleTimeChange = (selectedTimes, index) => {
        const updatedDatesTimes = [...formData.datesTimes];
        const currentDate = updatedDatesTimes[index].date;

        if (!currentDate) return;

        updatedDatesTimes[index] = {
            date: currentDate,
            times: selectedTimes.map(time => ({
                timeFrom: time.value.from,
                timeTo: time.value.to
            }))
        };

        setFormData({ ...formData, datesTimes: updatedDatesTimes });
    };

    const handleCustomTimeChange = (index, timeType, value) => {
        const updatedDatesTimes = [...formData.datesTimes];
        const currentEntry = updatedDatesTimes[index];

        if (!currentEntry.date) return;
        if (!currentEntry.times.length || timeType === 'timeFrom') {
            currentEntry.times = [{
                ...currentEntry.times[0],
                [timeType]: value
            }];
        } else {
            currentEntry.times[0][timeType] = value;
        }

        setFormData({ ...formData, datesTimes: updatedDatesTimes });
    };

    const toggleCustomTime = (index) => {
        const newCustomTimesEnabled = [...customTimesEnabled];
        newCustomTimesEnabled[index] = !newCustomTimesEnabled[index];
        setCustomTimesEnabled(newCustomTimesEnabled);
        const updatedDatesTimes = [...formData.datesTimes];
        updatedDatesTimes[index] = {
            ...updatedDatesTimes[index],
            times: [],
            custom: newCustomTimesEnabled[index],
        };
        setFormData({ ...formData, datesTimes: updatedDatesTimes });
    };

    const addDateTime = () => {
        setFormData({
            ...formData,
            datesTimes: [...formData.datesTimes, { date: "", times: [], custom: false }]
        });
        setCustomTimesEnabled([...customTimesEnabled, false]);
    };

    const removeDateTime = (index) => {
        const updatedDatesTimes = formData.datesTimes.filter((_, i) => i !== index);
        const updatedCustomTimesEnabled = customTimesEnabled.filter((_, i) => i !== index);
        setFormData({ ...formData, datesTimes: updatedDatesTimes });
        setCustomTimesEnabled(updatedCustomTimesEnabled);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const lessonPayload = {
            course: { courseId: formData.course },
            classroom: formData.classroom ? { classroomId: formData.classroom.value } : null,
            lecturer: formData.lecturer ? { userId: formData.lecturer.value } : null,
            online: formData.online,
            onlineInformation: formData.onlineInformation,
            lessonGroup: formData.lessonGroup
        };

        try {
            const lessonResponse = await lessonServiceInstance.insert(lessonPayload);
            const lessonId = lessonResponse.data.lessonId;

            if (lessonId) {
                const allDateTimes = formData.datesTimes.flatMap(dt =>
                    dt.times.map(time => ({
                        lesson: { lessonId: lessonId },
                        date: dt.date,
                        timeFrom: time.timeFrom,
                        timeTo: time.timeTo,
                        custom: dt.custom
                    }))
                );

                await Promise.all(allDateTimes.map(dt =>
                    lessonDateTimeServiceInstance.insert(dt)
                ));
            }

            setMessage("Lesson created successfully!");
            setFormData({
                course: "",
                classroom: null,
                lecturer: null,
                datesTimes: [],
                online: false,
                onlineInformation: "",
                lessonGroup: ""
            });
            setCustomTimesEnabled([]);
        } catch (error) {
            console.error("Error creating lesson:", error);
            setMessage("Error creating lesson. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Create Lesson</h2>
                    {message && <Alert variant="success">{message}</Alert>}
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Course</Form.Label>
                            <Form.Select name="course" value={formData.course} onChange={handleChange} required>
                                <option value="">Select a Course</option>
                                {courses.map(course => (
                                    <option key={course.courseId} value={course.courseId}>{course.name}</option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Lesson Group</Form.Label>
                            <Form.Control
                                type="number"
                                name="lessonGroup"
                                value={formData.lessonGroup}
                                onChange={handleChange}
                                placeholder="Enter lesson group number"
                                min="1"
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Classroom</Form.Label>
                            <Select
                                options={classrooms}
                                value={formData.classroom}
                                onChange={handleSelectChange("classroom")}
                                placeholder="Select or search for a classroom"
                                isClearable
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Lecturer</Form.Label>
                            <Select
                                options={lecturers}
                                value={formData.lecturer}
                                onChange={handleSelectChange("lecturer")}
                                placeholder="Select or search for a lecturer"
                                isClearable
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Check type="checkbox" label="Is Online?" name="online" checked={formData.online} onChange={handleChange} />
                        </Form.Group>
                        {formData.online && <Form.Group className="mb-3"><Form.Label>Online Information</Form.Label><Form.Control type="text" name="onlineInformation" value={formData.onlineInformation} onChange={handleChange} required={formData.online} /></Form.Group>}
                        <Form.Group className="mb-3">
                            <Form.Label className="mb-3">Dates & Times</Form.Label>
                            {formData.datesTimes.map((dt, index) => (
                                <Row key={index} className="mb-2 align-items-center">
                                    <Col xs={3}>
                                        <DatePicker
                                            selected={dt.date ? new Date(dt.date) : null}
                                            onChange={(date) => handleDateChange(date, index)}
                                            className="form-control"
                                            placeholderText="Select Date"
                                        />
                                    </Col>
                                    <Col xs={1}>
                                        <Form.Check
                                            type="checkbox"
                                            label="Custom"
                                            checked={customTimesEnabled[index]}
                                            onChange={() => toggleCustomTime(index)}
                                            disabled={!dt.date}
                                        />
                                    </Col>
                                    <Col xs={7}>
                                        {!customTimesEnabled[index] ? (
                                            <Select
                                                isMulti
                                                options={defaultTimes}
                                                value={defaultTimes.filter(t =>
                                                    dt.times?.some(time =>
                                                        time.timeFrom === t.value.from &&
                                                        time.timeTo === t.value.to
                                                    )
                                                )}
                                                onChange={(selected) => handleTimeChange(selected, index)}
                                                placeholder="Select Times"
                                                isDisabled={!dt.date}
                                            />
                                        ) : (
                                            <Row>
                                                <Col>
                                                    <Form.Control
                                                        type="time"
                                                        value={dt.times[0]?.timeFrom || ""}
                                                        onChange={(e) => handleCustomTimeChange(index, 'timeFrom', e.target.value)}
                                                        disabled={!dt.date}
                                                    />
                                                </Col>
                                                <Col>
                                                    <Form.Control
                                                        type="time"
                                                        value={dt.times[0]?.timeTo || ""}
                                                        onChange={(e) => handleCustomTimeChange(index, 'timeTo', e.target.value)}
                                                        disabled={!dt.date}
                                                    />
                                                </Col>
                                            </Row>
                                        )}
                                    </Col>
                                    <Col xs={1} className="text-center">
                                        <Button
                                            variant="outline-danger"
                                            size="sm"
                                            onClick={() => removeDateTime(index)}
                                            className="p-1"
                                        >
                                            <X size={16} />
                                        </Button>
                                    </Col>
                                </Row>
                            ))}
                            <div className="mt-4">
                                <Button onClick={addDateTime}>Add Date</Button>
                            </div>
                        </Form.Group>
                        <Button variant="primary" type="submit" className="w-100">Create Lesson</Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default LessonCreate;
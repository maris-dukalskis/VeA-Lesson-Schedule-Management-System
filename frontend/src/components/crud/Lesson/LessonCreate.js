import React, { useState, useEffect } from "react";
import lessonServiceInstance from "../../../api/LessonService";
import lessonDateTimeServiceInstance from "../../../api/LessonDateTimeService";
import courseServiceInstance from "../../../api/CourseService";
import classroomServiceInstance from "../../../api/ClassroomService";
import lecturerServiceInstance from "../../../api/LecturerService";
import semesterServiceInstance from "../../../api/SemesterService";
import { Container, Card, Form, Button, Alert, Row, Col } from "react-bootstrap";
import Select from "react-select";
import { registerLocale, setDefaultLocale } from "react-datepicker";
import DatePicker from "react-datepicker";
import enGB from 'date-fns/locale/en-GB';
import "react-datepicker/dist/react-datepicker.css";
import { X } from "lucide-react";
import { addDays, format, isBefore, isEqual } from 'date-fns';

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

    registerLocale('en-GB', enGB);
    setDefaultLocale('en-GB');

    const [formData, setFormData] = useState({
        course: null,
        classroom: null,
        lecturer: null,
        semester: null,
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
    const [duplicateSettings, setDuplicateSettings] = useState([]);
    const [isDuplicateEnabled, setIsDuplicateEnabled] = useState([]);
    const [semesters, setSemesters] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [courseResponse, classroomResponse, lecturerResponse, semesterResponse] = await Promise.all([
                    courseServiceInstance.getAll(),
                    classroomServiceInstance.getAll(),
                    lecturerServiceInstance.getAll(),
                    semesterServiceInstance.getAll()
                ]);
                setCourses(courseResponse.data.map(course => ({ value: course.courseId, label: course.name })));
                setClassrooms(classroomResponse.data.map(classroom => ({ value: classroom.classroomId, label: `${classroom.building}${classroom.number}` })));
                setLecturers(lecturerResponse.data.map(lecturer => ({ value: lecturer.userId, label: lecturer.fullName })));
                setSemesters(semesterResponse.data.map(semester => ({ value: semester.semesterId, label: semester.name })));
            } catch (error) {
                console.error("Error fetching data", error);
            }
        };
        fetchData();
    }, []);

    const handleChange = (event) => {
        const { name, value, type, checked } = event.target;
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
        const updatedCustomTimesEnabled = [...customTimesEnabled];
        updatedCustomTimesEnabled[index] = !updatedCustomTimesEnabled[index];
        setCustomTimesEnabled(updatedCustomTimesEnabled);
        const updatedDatesTimes = [...formData.datesTimes];
        updatedDatesTimes[index] = {
            ...updatedDatesTimes[index],
            times: [],
            custom: updatedCustomTimesEnabled[index],
        };
        setFormData({ ...formData, datesTimes: updatedDatesTimes });
    };

    const addDateTime = () => {
        setFormData({
            ...formData,
            datesTimes: [...formData.datesTimes, { date: "", times: [], custom: false }]
        });
        setCustomTimesEnabled([...customTimesEnabled, false]);
        setDuplicateSettings([...duplicateSettings, { duplicateWeekly: true, duplicateBiWeekly: false, duplicateUntil: null }]);
        setIsDuplicateEnabled([...isDuplicateEnabled, false]);
    };

    const removeDateTime = (index) => {
        const updatedDatesTimes = formData.datesTimes.filter((_, i) => i !== index);
        const updatedCustomTimesEnabled = customTimesEnabled.filter((_, i) => i !== index);
        const updatedDuplicateSettings = duplicateSettings.filter((_, i) => i !== index);
        const updatedIsDuplicateEnabled = isDuplicateEnabled.filter((_, i) => i !== index);
        setFormData({ ...formData, datesTimes: updatedDatesTimes });
        setCustomTimesEnabled(updatedCustomTimesEnabled);
        setDuplicateSettings(updatedDuplicateSettings);
        setIsDuplicateEnabled(updatedIsDuplicateEnabled);
    };

    const handleDuplicateChange = (index, field, value) => {
        setDuplicateSettings(prevSettings => {
            const updatedSettings = [...prevSettings];
            if (!updatedSettings[index]) return prevSettings;

            updatedSettings[index][field] = value;

            if (field === 'duplicateWeekly' && value) {
                updatedSettings[index].duplicateBiWeekly = false;
            } else if (field === 'duplicateBiWeekly' && value) {
                updatedSettings[index].duplicateWeekly = false;
            }

            return updatedSettings;
        });
    };

    const handleDuplicateUntilChange = (date, index) => {
        setDuplicateSettings(prevSettings => {
            const updatedSettings = [...prevSettings];
            if (!updatedSettings[index]) return prevSettings;

            if (date) {
                date.setHours(12, 0, 0, 0);
            }
            updatedSettings[index].duplicateUntil = date ? date.toISOString().split('T')[0] : null;
            return updatedSettings;
        });
    };

    const handleDuplicateToggle = (index, value) => {
        setIsDuplicateEnabled(prevIsDuplicateEnabled => {
            const updatedIsDuplicateEnabled = [...prevIsDuplicateEnabled];
            updatedIsDuplicateEnabled[index] = value;
            return updatedIsDuplicateEnabled;
        });

        setDuplicateSettings(prevDuplicateSettings => {
            const updatedDuplicateSettings = [...prevDuplicateSettings];
            if (value) {
                if (!updatedDuplicateSettings[index]) {
                    updatedDuplicateSettings[index] = { duplicateWeekly: true, duplicateBiWeekly: false, duplicateUntil: null };
                }
            } else {
                return updatedDuplicateSettings.filter((_, i) => i !== index);
            }
            return updatedDuplicateSettings;
        });
    };

    const handleConfirmDuplicate = (index) => {
        const currentDateTime = formData.datesTimes[index];
        const settings = duplicateSettings[index];
        const newDatesTimes = [];
        if (settings.duplicateWeekly || settings.duplicateBiWeekly) {
            let nextDate = new Date(currentDateTime.date);
            let endDate = new Date(settings.duplicateUntil);
            const interval = settings.duplicateWeekly ? 7 : 14;
            nextDate.setHours(12, 0, 0, 0);
            endDate.setHours(12, 0, 0, 0);
            while (isBefore(addDays(nextDate, interval), endDate) || isEqual(addDays(nextDate, interval), endDate)) {
                nextDate = addDays(nextDate, interval);
                newDatesTimes.push({
                    date: format(nextDate, 'yyyy-MM-dd'),
                    times: currentDateTime.times,
                    custom: currentDateTime.custom
                });
            }
        }
        const updatedDatesTimes = [...formData.datesTimes];
        updatedDatesTimes.push(...newDatesTimes);
        setFormData({ ...formData, datesTimes: updatedDatesTimes });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const lessonPayload = {
            course: formData.course ? { courseId: formData.course.value } : null,
            semester: formData.semester ? { semesterId: formData.semester.value } : null,
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
                const allDateTimes = formData.datesTimes.flatMap(dateTimeEntry =>
                    dateTimeEntry.times.map(time => ({
                        lesson: { lessonId: lessonId },
                        date: dateTimeEntry.date,
                        timeFrom: time.timeFrom,
                        timeTo: time.timeTo,
                        custom: dateTimeEntry.custom
                    }))
                );

                await Promise.all(allDateTimes.map(dateTime =>
                    lessonDateTimeServiceInstance.insert(dateTime)
                ));
            }

            setMessage("Lesson created successfully!");
            setFormData({
                course: null,
                semester: null,
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
                            <Select
                                options={courses}
                                value={formData.course}
                                onChange={handleSelectChange("course")}
                                placeholder="Select or search for a course"
                                isClearable
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Semester</Form.Label>
                            <Select
                                options={semesters}
                                value={formData.semester}
                                onChange={handleSelectChange("semester")}
                                placeholder="Select a semester"
                                required
                            />
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
                            {formData.datesTimes.map((dateTime, index) => (
                                <Row key={index} className="mb-2 align-items-center">
                                    <Col xs={3}>
                                        <DatePicker
                                            selected={dateTime.date ? new Date(dateTime.date) : null}
                                            onChange={(date) => handleDateChange(date, index)}
                                            className="form-control"
                                            placeholderText="Select Date"
                                            dateFormat="dd/MM/yyyy"
                                            locale="en-GB"
                                            weekStartsOn={1}
                                        />
                                    </Col>
                                    <Col xs={1}>
                                        <Form.Check
                                            type="checkbox"
                                            label="Custom"
                                            checked={customTimesEnabled[index]}
                                            onChange={() => toggleCustomTime(index)}
                                            disabled={!dateTime.date}
                                        />
                                    </Col>
                                    <Col xs={5}>
                                        {!customTimesEnabled[index] ? (
                                            <Select
                                                isMulti
                                                options={defaultTimes}
                                                value={defaultTimes.filter(defaultTime =>
                                                    dateTime.times?.some(time =>
                                                        time.timeFrom === defaultTime.value.from &&
                                                        time.timeTo === defaultTime.value.to
                                                    )
                                                )}
                                                onChange={(selected) => handleTimeChange(selected, index)}
                                                placeholder="Select Times"
                                                isDisabled={!dateTime.date}
                                            />
                                        ) : (
                                            <Row>
                                                <Col>
                                                    <Form.Control
                                                        type="time"
                                                        value={dateTime.times[0]?.timeFrom || ""}
                                                        onChange={(event) => handleCustomTimeChange(index, 'timeFrom', event.target.value)}
                                                        disabled={!dateTime.date}
                                                    />
                                                </Col>
                                                <Col>
                                                    <Form.Control
                                                        type="time"
                                                        value={dateTime.times[0]?.timeTo || ""}
                                                        onChange={(event) => handleCustomTimeChange(index, 'timeTo', event.target.value)}
                                                        disabled={!dateTime.date}
                                                    />
                                                </Col>
                                            </Row>
                                        )}
                                    </Col>
                                    <Col xs={1}>
                                        <Form.Check
                                            type="checkbox"
                                            label="Duplicate"
                                            checked={isDuplicateEnabled[index]}
                                            onChange={(e) => handleDuplicateToggle(index, e.target.checked)}
                                        />
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
                                    {isDuplicateEnabled[index] ? (
                                        <div style={{ marginTop: '10px' }}>
                                            <Row className="mb-2">
                                                <Col xs={2}>
                                                    <Form.Check
                                                        type="radio"
                                                        label="Weekly"
                                                        name={`duplicate-${index}`}
                                                        checked={duplicateSettings[index]?.duplicateWeekly}
                                                        onChange={() => handleDuplicateChange(index, 'duplicateWeekly', true)}
                                                    />
                                                </Col>
                                                <Col xs={2}>
                                                    <Form.Check
                                                        type="radio"
                                                        label="Bi-Weekly"
                                                        name={`duplicate-${index}`}
                                                        checked={duplicateSettings[index]?.duplicateBiWeekly}
                                                        onChange={() => handleDuplicateChange(index, 'duplicateBiWeekly', true)}
                                                    />
                                                </Col>
                                                <Col xs={3}>
                                                    <DatePicker
                                                        selected={duplicateSettings[index]?.duplicateUntil ? new Date(duplicateSettings[index]?.duplicateUntil) : null}
                                                        onChange={(date) => handleDuplicateUntilChange(date, index)}
                                                        className="form-control"
                                                        placeholderText="Duplicate Until"
                                                        dateFormat="dd/MM/yyyy"
                                                        locale="en-GB"
                                                        weekStartsOn={1}
                                                    />
                                                </Col>
                                                <Col xs={2}>
                                                    <Button size="sm" onClick={() => handleConfirmDuplicate(index)}>Confirm</Button>
                                                </Col>
                                            </Row>
                                        </div>
                                    ) : null}
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
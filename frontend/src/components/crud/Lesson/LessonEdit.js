import React, { useState, useEffect } from "react";
import lessonServiceInstance from "../../../api/LessonService";
import lessonDateTimeServiceInstance from "../../../api/LessonDateTimeService";
import courseServiceInstance from "../../../api/CourseService";
import classroomServiceInstance from "../../../api/ClassroomService";
import lecturerServiceInstance from "../../../api/LecturerService";
import { Container, Card, Form, Button, Alert, Row, Col } from "react-bootstrap";
import Select from "react-select";
import { registerLocale, setDefaultLocale } from "react-datepicker";
import DatePicker from "react-datepicker";
import enGB from 'date-fns/locale/en-GB';
import "react-datepicker/dist/react-datepicker.css";
import { X } from "lucide-react";
import { useParams } from "react-router-dom";
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

const LessonEdit = () => {

    registerLocale('en-GB', enGB);
    setDefaultLocale('en-GB');

    const { id } = useParams();
    const [formData, setFormData] = useState({
        course: null,
        classroom: null,
        lecturer: null,
        datesTimes: [],
        online: false,
        onlineInformation: "",
        lessonGroup: ""
    });
    const [courses, setCourses] = useState([]);
    const [classrooms, setClassrooms] = useState([]);
    const [lecturers, setLecturers] = useState([]);
    const [message, setMessage] = useState("");
    const [customTimesEnabled, setCustomTimesEnabled] = useState([]);
    const [loading, setLoading] = useState(true);
    const [deletedDateTimes, setDeletedDateTimes] = useState([]);
    const [duplicateSettings, setDuplicateSettings] = useState([]);
    const [isDuplicateEnabled, setIsDuplicateEnabled] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [courseResponse, classroomResponse, lecturerResponse, lessonResponse, lessonTimeResponse] = await Promise.all([
                    courseServiceInstance.getAll(),
                    classroomServiceInstance.getAll(),
                    lecturerServiceInstance.getAll(),
                    lessonServiceInstance.getById(id),
                    lessonDateTimeServiceInstance.getAllByLessonId(id),
                ]);

                setCourses(courseResponse.data.map(course => ({ value: course.courseId, label: course.name })));
                setClassrooms(classroomResponse.data.map(classroom => ({ value: classroom.classroomId, label: `${classroom.building}${classroom.number}` })));
                setLecturers(lecturerResponse.data.map(lecturer => ({ value: lecturer.userId, label: lecturer.fullName })));

                const lessonData = lessonResponse.data;
                const lessonTimes = lessonTimeResponse.data;

                const groupedDates = lessonTimes.reduce((accumulator, lessonTime) => {
                    const date = lessonTime.date;
                    if (!accumulator[date]) {
                        accumulator[date] = {
                            date: date,
                            times: [],
                            custom: lessonTime.custom,
                        };
                    }
                    accumulator[date].times.push({
                        timeFrom: lessonTime.timeFrom,
                        timeTo: lessonTime.timeTo,
                        lessonDateTimeId: lessonTime.lessonDateTimeId,
                    });
                    return accumulator;
                }, {});

                const datesTimesData = Object.values(groupedDates).map(group => ({
                    date: group.date,
                    times: group.times,
                    custom: group.custom,
                }));

                datesTimesData.sort((a, b) => new Date(a.date) - new Date(b.date));

                setFormData({
                    course: lessonData.course ? {
                        value: lessonData.course.courseId,
                        label: lessonData.course.name
                    } : null,
                    classroom: lessonData.classroom ? {
                        value: lessonData.classroom.classroomId,
                        label: `${lessonData.classroom.building}${lessonData.classroom.number}`
                    } : null,
                    lecturer: lessonData.lecturer ? {
                        value: lessonData.lecturer.userId,
                        label: lessonData.lecturer.fullName
                    } : null,
                    online: lessonData.online,
                    onlineInformation: lessonData.onlineInformation || "",
                    datesTimes: datesTimesData,
                    lessonGroup: lessonData.lessonGroup,
                });
                setCustomTimesEnabled(datesTimesData.map(dataTime => dataTime.custom));

                setLoading(false);
            } catch (error) {
                console.error("Error fetching data", error);
                setMessage("Error fetching lesson details.");
                setLoading(false);
            }
        };

        fetchData();
    }, [id]);

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
        const originalTimes = updatedDatesTimes[index].times;

        if (!currentDate) return;

        const newTimes = selectedTimes.map(time => ({
            timeFrom: time.value.from,
            timeTo: time.value.to,
            lessonDateTimeId: originalTimes.find(originalTime => originalTime.timeFrom === time.value.from && originalTime.timeTo === time.value.to)?.lessonDateTimeId,
        }));

        const removedTimes = originalTimes.filter(originalTime => !newTimes.find(newTime => newTime.timeFrom === originalTime.timeFrom && newTime.timeTo === originalTime.timeTo));
        removedTimes.forEach(removedTime => {
            if (removedTime.lessonDateTimeId) {
                setDeletedDateTimes(previous => [...previous, removedTime.lessonDateTimeId]);
            }
        });

        updatedDatesTimes[index] = {
            date: currentDate,
            times: newTimes,
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
            custom: newCustomTimesEnabled[index]
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
        const dateTimeToRemove = formData.datesTimes[index];

        if (dateTimeToRemove.lessonDateTimeId) {
            setDeletedDateTimes([...deletedDateTimes, dateTimeToRemove.lessonDateTimeId]);
        }

        const updatedDatesTimes = formData.datesTimes.filter((_, i) => i !== index);
        const updatedCustomTimesEnabled = customTimesEnabled.filter((_, i) => i !== index);
        setFormData({ ...formData, datesTimes: updatedDatesTimes });
        setCustomTimesEnabled(updatedCustomTimesEnabled);
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

                const newTimes = currentDateTime.times.map(time => ({
                    timeFrom: time.timeFrom,
                    timeTo: time.timeTo,
                }));

                newDatesTimes.push({
                    date: format(nextDate, 'yyyy-MM-dd'),
                    times: newTimes,
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
            classroom: formData.classroom ? { classroomId: formData.classroom.value } : null,
            lecturer: formData.lecturer ? { userId: formData.lecturer.value } : null,
            online: formData.online,
            onlineInformation: formData.onlineInformation,
            lessonGroup: formData.lessonGroup
        };

        try {
            await lessonServiceInstance.update(id, lessonPayload);

            const allTimeSlots = formData.datesTimes.flatMap(dateTime =>
                dateTime.times.map(time => ({
                    ...time,
                    date: dateTime.date,
                    custom: dateTime.custom,
                }))
            );

            console.log(allTimeSlots);

            const updates = allTimeSlots.filter(timeSlot => timeSlot.lessonDateTimeId);
            const inserts = allTimeSlots.filter(timeSlot => !timeSlot.lessonDateTimeId);

            const updateDateTimes = updates.map(timeSlot => ({
                lessonDateTimeId: timeSlot.lessonDateTimeId,
                lesson: { lessonId: id },
                date: timeSlot.date,
                timeFrom: timeSlot.timeFrom,
                timeTo: timeSlot.timeTo,
                custom: timeSlot.custom,
            }));

            const insertDateTimes = inserts.map(timeSlot => ({
                lesson: { lessonId: id },
                date: timeSlot.date,
                timeFrom: timeSlot.timeFrom,
                timeTo: timeSlot.timeTo,
                custom: timeSlot.custom,
            }));
            await Promise.all(deletedDateTimes.map(lessonDateTimeId =>
                lessonDateTimeServiceInstance.delete(lessonDateTimeId)
            ));

            setDeletedDateTimes([]);
            await Promise.all(updateDateTimes.map(dateTime =>
                lessonDateTimeServiceInstance.update(dateTime.lessonDateTimeId, dateTime)
            ));
            await Promise.all(insertDateTimes.map(dateTime =>
                lessonDateTimeServiceInstance.insert(dateTime)
            ));

            setMessage("Lesson updated successfully!");
        } catch (error) {
            console.error("Error updating lesson:", error);
            setMessage("Error updating lesson. Please try again.");
        }
    };

    return (
        <Container className="mt-5">
            <Card className="shadow-lg p-4">
                <Card.Body>
                    <h2 className="text-center mb-4">Edit Lesson</h2>
                    {message && <Alert variant={message.includes("Error") ? "danger" : "success"}>{message}</Alert>}
                    {loading ? (
                        <p className="text-center">Loading...</p>
                    ) : (
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
                                <Form.Label>Lesson Group</Form.Label>
                                <Form.Control
                                    type="number"
                                    name="lessonGroup"
                                    value={formData.lessonGroup}
                                    onChange={handleChange}
                                    placeholder="Enter lesson group number"
                                    min="0"
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
                            <Button variant="primary" type="submit" className="w-100">Update Lesson</Button>
                        </Form>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default LessonEdit;
import React, { useState, useEffect, useRef } from "react";
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import lessonServiceInstance from "../api/LessonService";
import lecturerServiceInstance from "../api/LecturerService";
import classroomServiceInstance from "../api/ClassroomService";
import studyProgrammeServiceInstance from "../api/StudyProgrammeService";
import lessonDateTimeServiceInstance from "../api/LessonDateTimeService";
import { Form, Row, Col, Container, Nav, Tab } from 'react-bootstrap';

const dayNames = {
  0: 'Svētdiena',
  1: 'Pirmdiena',
  2: 'Otrdiena',
  3: 'Trešdiena',
  4: 'Ceturtdiena',
  5: 'Piektdiena',
  6: 'Sestdiena'
};

const LessonCalendar = () => {

  const [studyProgrammes, setStudyProgrammes] = useState([]);
  const [selectedStudyProgrammeName, setSelectedStudyProgrammeName] = useState(null);
  const [selectedStudyProgrammeYear, setSelectedStudyProgrammeYear] = useState(null);
  const [studyProgrammeYears, setStudyProgrammeYears] = useState([]);
  const [events, setEvents] = useState([]);
  const [uniqueStudyProgrammeNames, setUniqueStudyProgrammeNames] = useState([]);
  const isFirstRender = useRef(true);
  const [lecturers, setLecturers] = useState([]);
  const [selectedLecturer, setSelectedLecturer] = useState(null);
  const [activeTab, setActiveTab] = useState('programme');
  const [currentView, setCurrentView] = useState('dayGridMonth');
  const [classrooms, setClassrooms] = useState([]);
  const [selectedClassroom, setSelectedClassroom] = useState(null);

  useEffect(() => {
    const fetchStudyProgrammes = async () => {
      try {
        const response = await studyProgrammeServiceInstance.getAll();
        setStudyProgrammes(response.data);
        const names = [...new Set(response.data.map(studyProgramme => studyProgramme.name))];
        setUniqueStudyProgrammeNames(names);
      } catch (error) {
        console.error("Error fetching study programmes", error);
      }
    };

    const fetchLecturers = async () => {
      try {
        const response = await lecturerServiceInstance.getAll();
        setLecturers(response.data);
      } catch (error) {
        console.error("Error fetching lecturers", error);
      }
    };

    const fetchClassrooms = async () => {
      try {
        const response = await classroomServiceInstance.getAll();
        setClassrooms(response.data);
      } catch (error) {
        console.error("Error fetching classrooms", error);
      }
    };

    fetchStudyProgrammes();
    fetchLecturers();
    fetchClassrooms();
  }, []);

  useEffect(() => {
    if (selectedStudyProgrammeName) {
      const years = studyProgrammes
        .filter(studyProgramme => studyProgramme.name === selectedStudyProgrammeName)
        .map(studyProgramme => studyProgramme.year);

      const uniqueYears = [...new Set(years)];
      setStudyProgrammeYears(uniqueYears);

      if (uniqueYears.length === 1) {
        setSelectedStudyProgrammeYear(uniqueYears[0]);
      } else {
        setSelectedStudyProgrammeYear(null);
      }
    } else {
      setStudyProgrammeYears([]);
      setSelectedStudyProgrammeYear(null);
    }
  }, [selectedStudyProgrammeName, studyProgrammes]);

  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false;
      return;
    }

    if (activeTab === 'programme') {
      if (selectedStudyProgrammeName && selectedStudyProgrammeYear) {
        handleShowLessonsByProgramme();
      }
    } else if (activeTab === 'lecturer') {
      if (selectedLecturer) {
        handleShowLessonsByLecturer();
      }
    } else if (activeTab === 'classroom') {
      if (selectedClassroom) {
        handleShowLessonsByClassroom();
      }
    }
  }, [selectedStudyProgrammeName, selectedStudyProgrammeYear, selectedLecturer, activeTab, selectedClassroom]);

  const fetchLessonDateTimes = async (lessons) => {
    const allLessonDateTimes = await Promise.all(
      lessons.map(async (lesson) => {
        const ldtResponse = await lessonDateTimeServiceInstance.getAllByLessonId(lesson.lessonId);
        return { lessonId: lesson.lessonId, lessonDateTimes: ldtResponse.data };
      })
    );

    const ldtMap = {};
    allLessonDateTimes.forEach(({ lessonId, lessonDateTimes }) => {
      ldtMap[lessonId] = lessonDateTimes;
    });
    return ldtMap;
  };
  const convertToEvents = (lessons, lessonDateTimesMap) => {
    const allEvents = [];

    lessons.forEach(lesson => {
      const lessonDateTimesForLesson = lessonDateTimesMap[lesson.lessonId] || [];
      const lecturerFirstName = lesson.lecturer?.fullName[0] || "";
      const lecturerLastName = lesson.lecturer?.fullName.split(" ")[1] || "";

      lessonDateTimesForLesson.forEach(lessonDateTime => {
        const from = new Date(`1970-01-01T${lessonDateTime.timeFrom}`);
        const to = new Date(`1970-01-01T${lessonDateTime.timeTo}`);
        const fromHours = from.getHours().toString().padStart(2, '0');
        const fromMinutes = from.getMinutes().toString().padStart(2, '0');
        const toHours = to.getHours().toString().padStart(2, '0');
        const toMinutes = to.getMinutes().toString().padStart(2, '0');

        const timeDisplay = `${fromHours}:${fromMinutes} - ${toHours}:${toMinutes}`;
        const classroomInfo = lesson.classroom ? lesson.classroom.building + lesson.classroom.number : 'Online';
        const lecturerInfo = lesson.lecturer ? lesson.lecturer.seniority + " " + lecturerFirstName + ". " + lecturerLastName : 'TBA';

        const title = `${timeDisplay} ${classroomInfo} ${lesson.course.name} (${lecturerInfo})`;

        allEvents.push({
          title: title,
          start: `${lessonDateTime.date}T${lessonDateTime.timeFrom}`,
          end: `${lessonDateTime.date}T${lessonDateTime.timeTo}`
        });
      });
    });

    return allEvents;
  };

  const handleStudyProgrammeNameChange = (event) => {
    setSelectedStudyProgrammeName(event.target.value);
  };

  const handleStudyProgrammeYearChange = (event) => {
    setSelectedStudyProgrammeYear(parseInt(event.target.value));
  };

  const handleTabSelect = (key) => {
    setActiveTab(key);
    setEvents([]);
  };

  const handleLecturerChange = (event) => {
    setSelectedLecturer(event.target.value);
  };

  const handleClassroomChange = (event) => {
    setSelectedClassroom(event.target.value);
  };

  const handleShowLessonsByLecturer = async () => {
    if (!selectedLecturer) return;

    try {
      const response = await lessonServiceInstance.getByLecturerName(selectedLecturer);

      const ldtMap = await fetchLessonDateTimes(response.data);

      const eventData = convertToEvents(response.data, ldtMap);
      setEvents(eventData);
    } catch (error) {
      console.error("Error fetching lessons by lecturer", error);
    }
  };

  const handleShowLessonsByProgramme = async () => {
    if (!selectedStudyProgrammeName || !selectedStudyProgrammeYear) return;

    try {
      const response = await lessonServiceInstance.getByStudyProgrammeNameAndYear(selectedStudyProgrammeName, selectedStudyProgrammeYear);
      const filteredLessons = response.data;

      const ldtMap = await fetchLessonDateTimes(filteredLessons);

      const eventData = convertToEvents(filteredLessons, ldtMap);
      setEvents(eventData);
    } catch (error) {
      console.error("Error fetching lessons by programme", error);
    }
  };

  const handleShowLessonsByClassroom = async () => {
    if (!selectedClassroom) return;

    const [building, number] = selectedClassroom.split(' ');

    try {
      const response = await lessonServiceInstance.getByClassroomBuildingAndNumber(building, number);
      const filteredLessons = response.data;

      const ldtMap = await fetchLessonDateTimes(filteredLessons);

      const eventData = convertToEvents(filteredLessons, ldtMap);
      setEvents(eventData);
    } catch (error) {
      console.error("Error fetching lessons by classroom", error);
    }
  };

  return (
    <Container className="mt-4">
      <div style={{ border: '1px solid #ced4da', padding: '15px', borderRadius: '5px', marginBottom: '1rem' }}>
        <Tab.Container id="selection-tabs" activeKey={activeTab} onSelect={handleTabSelect}>
          <Nav variant="tabs" className="mb-3">
            <Nav.Item>
              <Nav.Link eventKey="programme">Study Programme</Nav.Link>
            </Nav.Item>
            <Nav.Item>
              <Nav.Link eventKey="lecturer">Lecturer</Nav.Link>
            </Nav.Item>
            <Nav.Item>
              <Nav.Link eventKey="classroom">Classroom</Nav.Link>
            </Nav.Item>
          </Nav>
          <Tab.Content>
            <Tab.Pane eventKey="programme">
              <Form className="mb-0">
                <Row className="align-items-center">
                  <Col xs={12} md={4}>
                    <Form.Group>
                      <Form.Label>Study Programme</Form.Label>
                      <Form.Control
                        as="select"
                        value={selectedStudyProgrammeName || ""}
                        onChange={handleStudyProgrammeNameChange}
                      >
                        <option value="" disabled hidden>Select Study Programme</option>
                        {uniqueStudyProgrammeNames.map(name => (
                          <option key={name} value={name}>{name}</option>
                        ))}
                      </Form.Control>
                    </Form.Group>
                  </Col>
                  <Col xs={12} md={2}>
                    {studyProgrammeYears.length > 0 && (
                      <Form.Group>
                        <Form.Label>Year</Form.Label>
                        <Form.Control
                          as="select"
                          value={selectedStudyProgrammeYear || ""}
                          onChange={handleStudyProgrammeYearChange}
                        >
                          <option value="" disabled hidden>Select Year</option>
                          {studyProgrammeYears.map(year => (
                            <option key={year} value={year}>{year}</option>
                          ))}
                        </Form.Control>
                      </Form.Group>
                    )}
                  </Col>
                </Row>
              </Form>
            </Tab.Pane>
            <Tab.Pane eventKey="lecturer">
              <Form className="mb-0">
                <Row className="align-items-center">
                  <Col xs={12} md={4}>
                    <Form.Group>
                      <Form.Label>Lecturer</Form.Label>
                      <Form.Control
                        as="select"
                        value={selectedLecturer || ""}
                        onChange={handleLecturerChange}
                      >
                        <option value="" disabled hidden>Select Lecturer</option>
                        {lecturers.map(lecturer => (
                          <option key={lecturer.id} value={lecturer.fullName}>{lecturer.fullName}</option>
                        ))}
                      </Form.Control>
                    </Form.Group>
                  </Col>
                </Row>
              </Form>
            </Tab.Pane>
            <Tab.Pane eventKey="classroom">
              <Form className="mb-0">
                <Row className="align-items-center">
                  <Col xs={12} md={4}>
                    <Form.Group>
                      <Form.Label>Classroom</Form.Label>
                      <Form.Control
                        as="select"
                        value={selectedClassroom || ""}
                        onChange={handleClassroomChange}
                      >
                        <option value="" disabled hidden>Select Classroom</option>
                        {classrooms.map(classroom => (
                          <option key={classroom.id} value={`${classroom.building} ${classroom.number}`}>
                            {`${classroom.building} ${classroom.number}`}
                          </option>
                        ))}
                      </Form.Control>
                    </Form.Group>
                  </Col>
                </Row>
              </Form>
            </Tab.Pane>
          </Tab.Content>
        </Tab.Container>
      </div>

      <FullCalendar
        plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
        initialView={currentView}
        slotMinTime="09:00:00"
        slotMaxTime="20:55:00"
        slotLabelInterval='00:30'
        allDaySlot={false}
        eventDisplay="block"
        displayEventTime={false}
        events={events}
        dayMaxEventRows={false}
        height="auto"
        firstDay={1}
        fixedWeekCount={false}
        slotLabelFormat={{
          hour: '2-digit',
          minute: '2-digit',
          hour12: false,
        }}
        eventTimeFormat={{
          hour: '2-digit',
          minute: '2-digit',
          hour12: false
        }}
        views={{
          dayGridMonth: { buttonText: 'Mēnesis' },
          timeGridWeek: { buttonText: 'Nedēļa' },
          timeGridDay: { buttonText: 'Diena' }
        }}
        headerToolbar={{
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay'
        }}

        dayHeaderContent={(args) => {
          const weekday = args.date.getDay();
          return dayNames[weekday];
        }}
        viewDidMount={(view) => {
          setCurrentView(view.view.type);
        }}
      />
    </Container>
  );
};

export default LessonCalendar;
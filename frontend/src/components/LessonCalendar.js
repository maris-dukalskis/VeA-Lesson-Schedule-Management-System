import React, { useState, useEffect, useRef } from "react";
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import lessonServiceInstance from "../api/LessonService";
import studyProgrammeServiceInstance from "../api/StudyProgrammeService";
import lessonDateTimeServiceInstance from "../api/LessonDateTimeService";
import { Form, Button, Row, Col, Container } from 'react-bootstrap';

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
  const [lessonDateTimes, setLessonDateTimes] = useState({});
  const [studyProgrammes, setStudyProgrammes] = useState([]);
  const [selectedStudyProgrammeName, setSelectedStudyProgrammeName] = useState(null);
  const [selectedStudyProgrammeYear, setSelectedStudyProgrammeYear] = useState(null);
  const [studyProgrammeYears, setStudyProgrammeYears] = useState([]);
  const [events, setEvents] = useState([]);
  const [uniqueStudyProgrammeNames, setUniqueStudyProgrammeNames] = useState([]);
  const isFirstRender = useRef(true);

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
    fetchStudyProgrammes();
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

  const handleStudyProgrammeNameChange = (event) => {
    setSelectedStudyProgrammeName(event.target.value);
  };

  const handleStudyProgrammeYearChange = (event) => {
    setSelectedStudyProgrammeYear(parseInt(event.target.value));
  };

  const handleShowLessons = async () => {
    if (!selectedStudyProgrammeName || !selectedStudyProgrammeYear) return;

    try {
      const response = await lessonServiceInstance.getByStudyProgrammeNameAndYear(selectedStudyProgrammeName, selectedStudyProgrammeYear);
      const filteredLessons = response.data;

      const allLessonDateTimes = await Promise.all(
        filteredLessons.map(async (lesson) => {
          const ldtResponse = await lessonDateTimeServiceInstance.getAllByLessonId(lesson.lessonId);
          return { lessonId: lesson.lessonId, lessonDateTimes: ldtResponse.data };
        })
      );

      const ldtMap = {};
      allLessonDateTimes.forEach(({ lessonId, lessonDateTimes }) => {
        ldtMap[lessonId] = lessonDateTimes;
      });
      setLessonDateTimes(ldtMap);

      const eventData = filteredLessons.flatMap(lesson => {
        const lessonDateTimesForLesson = lessonDateTimes[lesson.lessonId] || [];
        return lessonDateTimesForLesson.map(lessonDateTime => {
          const from = new Date(`1970-01-01T${lessonDateTime.timeFrom}`);
          const to = new Date(`1970-01-01T${lessonDateTime.timeTo}`);
          const fromHours = from.getHours().toString().padStart(2, '0');
          const fromMinutes = from.getMinutes().toString().padStart(2, '0');
          const toHours = to.getHours().toString().padStart(2, '0');
          const toMinutes = to.getMinutes().toString().padStart(2, '0');

          const timeDisplay = `${fromHours}:${fromMinutes} - ${toHours}:${toMinutes}`;
          const title = `${timeDisplay} ${lesson.classroom ? lesson.classroom.building + lesson.classroom.number : 'Online'} ${lesson.course.name} (${lesson.lecturer ? lesson.lecturer.fullName : 'TBA'})`;

          return {
            title: title,
            date: lessonDateTime.date
          };
        });
      });
      setEvents(eventData);

    } catch (error) {
      console.error("Error fetching lessons", error);
    }
  };
  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false;
      return;
    }
    handleShowLessons();
  }, [selectedStudyProgrammeName, selectedStudyProgrammeYear]);

  return (
    <Container className="mt-4">
      <div style={{ border: '1px solid #ced4da', padding: '15px', borderRadius: '5px', marginBottom: '1rem' }}>
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
          <Row>
            <Col xs={12} className="mt-3">
              <Button variant="primary" onClick={handleShowLessons} disabled={!selectedStudyProgrammeName || !selectedStudyProgrammeYear}>
                Show
              </Button>
            </Col>
          </Row>
        </Form>
      </div>

      <FullCalendar
        plugins={[dayGridPlugin]}
        initialView="dayGridMonth"
        events={events}
        dayMaxEventRows={false}
        height="auto"
        firstDay={1}
        fixedWeekCount={false}
        dayHeaderContent={(args) => {
          const weekday = args.date.getDay();
          return dayNames[weekday];
        }}
      />
    </Container>
  );
};

export default LessonCalendar;
import logo from './logo.svg';
import './App.css';
import { useEffect } from 'react';

import { Route, Routes, useNavigate } from 'react-router-dom';

import ClassroomCreate from './components/crud/Classroom/ClassroomCreate';
import ClassroomList from './components/crud/Classroom/ClassroomList';
import ClassroomEdit from './components/crud/Classroom/ClassroomEdit';

import CourseCreate from './components/crud/Course/CourseCreate';
import CourseList from './components/crud/Course/CourseList';
import CourseEdit from './components/crud/Course/CourseEdit';

import StudyProgrammeCreate from './components/crud/StudyProgramme/StudyProgrammeCreate';
import StudyProgrammeList from './components/crud/StudyProgramme/StudyProgrammeList';
import StudyProgrammeEdit from './components/crud/StudyProgramme/StudyProgrammeEdit';

import UserCreate from './components/crud/User/UserCreate';
import UserList from './components/crud/User/UserList';
import UserEdit from './components/crud/User/UserEdit';

import LessonCreate from './components/crud/Lesson/LessonCreate';
import LessonList from './components/crud/Lesson/LessonList';
import LessonEdit from './components/crud/Lesson/LessonEdit';

import SemesterCreate from './components/crud/Semester/SemesterCreate';
import SemesterList from './components/crud/Semester/SemesterList';
import SemesterEdit from './components/crud/Semester/SemesterEdit';

import Logout from './components/auth/Logout';
import Admin from './components/Admin';
import Header from './components/Header';
import Footer from './components/Footer';

import LessonCalendar from './components/LessonCalendar';
import ProtectedRoute from './components/auth/ProtectedRoute';

import { StartTokenAutoRefresh } from './components/auth/TokenRefresh';

function App() {

  const navigate = useNavigate();

  useEffect(() => {
    StartTokenAutoRefresh(navigate);
  }, [navigate]);

  return (
    <>
      <Header />
      <div className='app-container'>
        <main className='main-content'>
          <div className='container'>
            <Routes>
              <Route element={<ProtectedRoute allowedRoles={["ADMINISTRATOR"]} />}>
                <Route path="/classroom/create" element={<ClassroomCreate />} />
                <Route path="/classroom/list" element={<ClassroomList />} />
                <Route path="/classroom/edit/:id" element={<ClassroomEdit />} />

                <Route path="/course/create" element={<CourseCreate />} />
                <Route path="/course/list" element={<CourseList />} />
                <Route path="/course/edit/:id" element={<CourseEdit />} />

                <Route path="/studyprogramme/create" element={<StudyProgrammeCreate />} />
                <Route path="/studyprogramme/list" element={<StudyProgrammeList />} />
                <Route path="/studyprogramme/edit/:id" element={<StudyProgrammeEdit />} />

                <Route path="/user/create" element={<UserCreate />} />
                <Route path="/user/list" element={<UserList />} />
                <Route path="/user/edit/:id" element={<UserEdit />} />

                <Route path="/lesson/create" element={<LessonCreate />} />
                <Route path="/lesson/list" element={<LessonList />} />
                <Route path="/lesson/edit/:id" element={<LessonEdit />} />

                <Route path="/semester/create" element={<SemesterCreate />} />
                <Route path="/semester/list" element={<SemesterList />} />
                <Route path="/semester/edit/:id" element={<SemesterEdit />} />
                <Route path="/admin" element={<Admin />} />
              </Route>

              <Route path="/logout" element={<Logout />} />
              <Route path="/" element={<LessonCalendar />} />
            </Routes>

          </div>
        </main>
      </div>
      <Footer />
    </>
  );
}

export default App;

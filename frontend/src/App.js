import logo from './logo.svg';
import './App.css';

import { Route, Routes } from 'react-router-dom';
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

import Login from './components/auth/Login';

function App() {
  return (
    <>
      {/* <Header /> */}
      <div className='app-container'>
        <main className='main-content'>
          <div className='container'>
            <Routes>
              {/* <Route path="/login" element={<Login />} />
              <Route path="/logout" element={<Logout />} /> */}
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

              <Route path="/login" element={<Login />} />
            </Routes>

          </div>
        </main>
      </div>
      {/* <Footer /> */}
    </>
  );
}

export default App;

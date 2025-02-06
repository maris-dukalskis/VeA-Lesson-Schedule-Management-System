import logo from './logo.svg';
import './App.css';
import ClassroomCreate from './components/crud/Classroom/ClassroomCreate';
import { Route, Routes } from 'react-router-dom';
import ClassroomList  from './components/crud/Classroom/ClassroomList';
import ClassroomUpdate from './components/crud/Classroom/ClassroomUpdate';


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
              <Route path="/classroom/update/:id" element={<ClassroomUpdate />} />
            </Routes>

          </div>
        </main>
      </div>
      {/* <Footer /> */}
    </>
  );
}

export default App;

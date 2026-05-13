// main.jsx

import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import SchedulerHelper from './SchedulerHelper.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <SchedulerHelper />
  </StrictMode>,
)

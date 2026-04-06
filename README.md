### Phase 1 (Complete):

- Create an effective way to communicate between front and backend: prevent sending large amount of data while offering seemless correlation between frontend UI and backend processes.

  ``
  Jobs are initialized when the backend first recieves a document from the frontend. These jobs persist until the entire procedure is complete and the user has successfully sorted their document. The jobs are codified into JobConfig objects, which are essentially just DTOs with relevant metadata for job completion and correlation (between the back- and front-end).
  ``

### Phase 2 (Complete):
- Allow users to request specific job processing into image files without concurrency issues.
  
  `
  Users can request to process the PDF into image files asynchronously.
  `
- Create lightweight, in-application caches to recover the location of the procedure in the case of the backend failing.

- Send images from the backend to the frontend for later requests.

### Phase 3 (In Progress):
- Allow user to request images from the backend

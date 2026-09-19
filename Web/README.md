# EnglishLog Web
React + TypeScript + Vite frontend for the English learning record project. The UI is intentionally close to an operational Australian product team app: searchable records, folders, image upload, registration/login, Google sign-in entry, and clickable interview skill links.

## Run
```bash
npm install
npm run dev
```
Open the Vite URL, normally http://localhost:5173. Start Backend first at http://localhost:5050. Set `VITE_API_URL` when the API is elsewhere.

## Build and deploy
`npm run build` produces `dist/`. Deploy it to Netlify, Vercel, Azure Static Web Apps or an Nginx static host. Configure `VITE_API_URL` at build time and enable CORS on the backend.

## Code map for interviews
- React state and data fetching: `src/main.tsx`, `App`
- Authentication form and OAuth redirect: `src/main.tsx`, `Auth`
- File upload: `src/main.tsx`, `addEntry`
- Responsive layout and accessibility-friendly controls: `src/styles.css`
- API boundary: `src/main.tsx`, `api<T>`
- Skills are clickable in the left rail and anchor to the code map at the bottom of the page.

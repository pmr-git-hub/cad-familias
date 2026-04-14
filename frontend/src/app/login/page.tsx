import { LoginBanner } from "./components/LoginBanner";
import { LoginForm } from "./components/LoginForm";

export default function LoginPage() {
  return (
    <div className="relative flex min-h-screen">
      <LoginBanner />
      <LoginForm />
    </div>
  );
}
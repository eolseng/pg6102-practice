import React, {useContext} from "react";
import {useRouter} from "next/router";
import Link from "next/link";

import Button from "react-bootstrap/Button";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";

import {UserContext} from "../../contexts/UserContext";
import {getUserData, logout} from "../../lib/auth";

export default function Header() {

    const {state} = useContext(UserContext)

    return (
        <Navbar bg={"light"} className={"d-flex justify-content-between align-items-center border-bottom"}>
            <div className={"container"}>
                <Link href={"/"}>
                    <Navbar.Brand href={"/"}>LOGO</Navbar.Brand>
                </Link>
                <Navbar.Text className={"align-self-center"}>
                    {!state.user && 'Welcome!'}
                    {state.user && 'Signed in as ' + state.user.username}
                </Navbar.Text>
                <Nav variant={"pills"} className="">
                    {!state.user &&
                    <>
                        <Link href={"/signup"}>
                            <Button className={"m-1"}>Signup</Button>
                        </Link>
                        <Link href={"/login"}>
                            <Button className={"m-1"}>Login</Button>
                        </Link>
                    </>
                    }
                    {state.user &&
                    <>
                        <LogoutButton/>
                    </>}
                </Nav>
            </div>
        </Navbar>
    )
};


export function LogoutButton() {

    const {dispatch} = useContext(UserContext)
    const router = useRouter()

    const handleLogout = async () => {
        const result = await logout()
        console.log(result)
        if (result) {
            await getUserData()
            dispatch({type: "setUser"})
            await router.push("/")
        }
    }

    return <Button onClick={handleLogout} className={"m-1"}>Logout</Button>

}
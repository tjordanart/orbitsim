START

Create window
Create planet
Create spacecraft

Set spacecraft starting position
Set spacecraft starting velocity
Set simulation state to RUNNING

WHILE simulation is running:

    Calculate distance between spacecraft and planet

    IF spacecraft hits planet:
        Set state to CRASHED
        Stop spacecraft

    ELSE IF spacecraft is far enough away:
        Set state to ESCAPED
        Stop spacecraft

    ELSE:
        Calculate gravitational acceleration
        Update spacecraft velocity
        Update spacecraft position
        Add current position to trajectory

    Check for mouse interaction

    IF user grabs spacecraft:
        Allow user to drag/flick spacecraft
        Calculate new velocity from flick
        Continue simulation

    Draw planet
    Draw spacecraft
    Draw trajectory
    Draw velocity information
    Draw simulation status

IF user presses Reset:
    Restore starting position
    Restore starting velocity
    Clear trajectory
    Set state to RUNNING

END

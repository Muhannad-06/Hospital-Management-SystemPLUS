package application;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AestheticAnimations {
	
	// Some Animations for Aesthtic Purposes
    public static void startHeartbeat(Pane container, String pulseColorHex ) {
        Path heartbeatPath = new Path();
        heartbeatPath.setStroke(Color.web(pulseColorHex));
        heartbeatPath.setStrokeWidth(4);

        DropShadow lineGlow = new DropShadow(12, javafx.scene.paint.Color.web(pulseColorHex));
        heartbeatPath.setEffect(lineGlow);

        
        heartbeatPath.getElements().addAll(
                new javafx.scene.shape.MoveTo(0, 40),
                new javafx.scene.shape.LineTo(40, 40),
                new javafx.scene.shape.LineTo(50, 5),   
                new javafx.scene.shape.LineTo(65, 75),  
                new javafx.scene.shape.LineTo(80, 40),  
                new javafx.scene.shape.LineTo(140, 40)
        );

        Circle glowingBall = new javafx.scene.shape.Circle(3, Color.ALICEBLUE);
        DropShadow ballGlow = new DropShadow(12, Color.web(pulseColorHex));
        glowingBall.setEffect(ballGlow);

        
        Pane animationHolder = new Pane();
        animationHolder.getChildren().addAll(heartbeatPath, glowingBall);

        // I've Binded The Placement to the Scene ==> Application of BINDING was in the last Lecture
        animationHolder.layoutXProperty().bind(container.widthProperty().subtract(180)); 
        animationHolder.layoutYProperty().bind(container.heightProperty().subtract(100)); 

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(javafx.util.Duration.seconds(3)); 
        pathTransition.setPath(heartbeatPath);
        pathTransition.setNode(glowingBall);
        pathTransition.setCycleCount(Timeline.INDEFINITE); 
        pathTransition.setAutoReverse(true);

        pathTransition.play();
        
        
        container.getChildren().add(animationHolder);
    }
  
    // Glow hover Animations For Aesthtic Reasons ======>AI involved in the CSS parts
    public static void applyGlassGlowAnimation(Button button) {
        if (button == null) return;

        // Make The Button Transparent
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: white;");

        // Create the path and give it the button properties
        Rectangle borderLight = new Rectangle();
        borderLight.setWidth(button.getPrefWidth());
        borderLight.setHeight(button.getPrefHeight());
        
        // corner curves matching the button 
        borderLight.setArcWidth(16); 
        borderLight.setArcHeight(16);
        
        // thin glass reflection 
        borderLight.setFill(Color.TRANSPARENT);
        borderLight.setStroke(Color.web("rgba(255, 255, 255, 0.65)")); 
        borderLight.setStrokeWidth(1.0); 
        
        // Completely hidden until hovered
        borderLight.setOpacity(0.0);

       
        
        double totalPerimeter = (button.getPrefWidth() + button.getPrefHeight()) * 2;
        borderLight.getStrokeDashArray().addAll(25.0, totalPerimeter);

        // white glow to give glass reflection 
        DropShadow reflectionGlow = new DropShadow();
        reflectionGlow.setColor(Color.WHITE);
        reflectionGlow.setRadius(4); // Tight, focused glow radius
        reflectionGlow.setSpread(0.1);
        borderLight.setEffect(reflectionGlow);

        // Align with the button
        borderLight.setLayoutX(button.getLayoutX());
        borderLight.setLayoutY(button.getLayoutY());
        
        // let the mouse ignores the overlay
        borderLight.setMouseTransparent(true);

        // Inject the dynamic layer node into the parent layout structure safely ====> AI assisted 
        // It stays the button on top so the mouse can still click it perfectly without any dead zones
        if (button.getParent() instanceof Pane) {
            Pane parent = (Pane) button.getParent();
            int buttonIndex = parent.getChildren().indexOf(button);
            parent.getChildren().add(buttonIndex, borderLight);
        }

        
        Timeline crawlingLightTimeline = new Timeline();
        crawlingLightTimeline.setCycleCount(Timeline.INDEFINITE);

        
        KeyValue kvOffset = new KeyValue(borderLight.strokeDashOffsetProperty(), totalPerimeter + 25.0, Interpolator.LINEAR);
        KeyFrame kfMove = new KeyFrame(Duration.seconds(3), kvOffset); 
        crawlingLightTimeline.getKeyFrames().add(kfMove);

        //  HOVER effect
        button.setOnMouseEntered(e -> {
            borderLight.setOpacity(1.0);  
            crawlingLightTimeline.play(); 
        });

        button.setOnMouseExited(e -> {
            crawlingLightTimeline.stop(); 
            borderLight.setOpacity(0.0);  
            borderLight.setStrokeDashOffset(0.0); 
        });
        
        button.setOnMousePressed(e -> {
            // Turn the thin border into a solid bright flash and intense glow
            borderLight.getStrokeDashArray().clear();
            borderLight.setStrokeWidth(1.5); 
            reflectionGlow.setRadius(12); 
            reflectionGlow.setColor(Color.web("#ffffff"));
        });

        
        button.setOnMouseReleased(e -> {
            // Restore the moving line pattern and gentle glass reflection state
            borderLight.getStrokeDashArray().addAll(25.0, totalPerimeter);
            borderLight.setStrokeWidth(1.0);
            reflectionGlow.setRadius(4);
        });
    }

    // Dynamic 3D-Rotating DNA Strand Animation for Medical Visuals =====> AI Did most of the work i've tried my best but it didn't work
    public static void startDNARotation(Pane container, double xPos, double yPos, String colorHex , Color ballColor) {
        Pane dnaHolder = new Pane();
        dnaHolder.setLayoutX(xPos);
        dnaHolder.setLayoutY(yPos);

        int totalNodes = 12;         // Number of base pairs along the strand
        double spacingX = 15.0;      // Horizontal distance between nodes
        double amplitude = 35.0;     // Height/Width of the 3D helix twist boundary
        Color baseColor = Color.web(colorHex);

        // Arrays to hold our graphic nodes so the Timeline can update them dynamically
        Circle[] strandA = new Circle[totalNodes];
        Circle[] strandB = new Circle[totalNodes];
        Rectangle[] rungs = new Rectangle[totalNodes];

        DropShadow neonGlow = new DropShadow(10, baseColor);

        // 1. Build the structural components of the DNA Ladder
        for (int i = 0; i < totalNodes; i++) {
            // Horizontal connecting rungs
            rungs[i] = new Rectangle(2, 2);
            rungs[i] = new Rectangle(0, 0, 1, 2); // Tiny initial canvas height
            rungs[i] = new Rectangle(0, 0, 2, 1);
            rungs[i].setFill(baseColor.deriveColor(0, 1, 1, 0.4)); // Semi-transparent rungs
            rungs[i].setEffect(neonGlow);

            // Strand A tracking node (White core with neon halo)
            strandA[i] = new Circle(4, ballColor);
            strandA[i].setEffect(neonGlow);

            // Strand B tracking node
            strandB[i] = new Circle(4, ballColor);
            strandB[i].setEffect(neonGlow);

            // Add elements to our localized display holder pane
            dnaHolder.getChildren().addAll(rungs[i], strandA[i], strandB[i]);
        }

        container.getChildren().add(dnaHolder);

        // 2. Setup the Mathematical Wave Translation Engine
        Timeline rotationClock = new Timeline();
        rotationClock.setCycleCount(Timeline.INDEFINITE);

        // Mutable tracking wrapper for step-by-step phase advancement
        final double[] phaseOffset = {0.0};

        KeyFrame frame = new KeyFrame(Duration.millis(30), event -> {
            phaseOffset[0] += 0.08; // Control rotation speed

            for (int i = 0; i < totalNodes; i++) {
                // Calculate individual node angles distributed along the length of the string
                double angle = (i * 0.6) + phaseOffset[0];

                // Sine mapping gives us the vertical 3D depth oscillation perspective
                double posA = Math.sin(angle) * amplitude;
                double posB = Math.sin(angle + Math.PI) * amplitude; // Perfect 180-degree phase inversion

                double currentX = i * spacingX;
                double baselineY = amplitude; // Zero line center baseline shift

                // Update particle tracking positions
                strandA[i].setCenterX(currentX);
                strandA[i].setCenterY(baselineY + posA);

                strandB[i].setCenterX(currentX);
                strandB[i].setCenterY(baselineY + posB);

                // Update vertical linking connecting rungs
                double rLeft = Math.min(posA, posB);
                double rWidth = Math.abs(posA - posB);

                rungs[i].setX(currentX);
                rungs[i].setY(baselineY + rLeft);
                rungs[i].setHeight(rWidth);
                rungs[i].setWidth(1.5); // Fixed line structural width

                // 3. PERSPECTIVE LAYER: Adjust node scales based on depth (Cos waves)
                // Creates a high-fidelity 3D layer mapping illusion
                double scaleA = (Math.cos(angle) + 1.0) / 2.0; // Normalized between 0.0 and 1.0
                double scaleB = (Math.cos(angle + Math.PI) + 1.0) / 2.0;

                strandA[i].setRadius(2.0 + (scaleA * 3.5)); // Shrinks when traveling "away" from perspective viewport
                strandB[i].setRadius(2.0 + (scaleB * 3.5));
                
                // Dim elements slightly in back plane for depth immersion
                strandA[i].setOpacity(0.4 + (scaleA * 0.6));
                strandB[i].setOpacity(0.4 + (scaleB * 0.6));
            }
        });

        rotationClock.getKeyFrames().add(frame);
        rotationClock.play();
    }
}

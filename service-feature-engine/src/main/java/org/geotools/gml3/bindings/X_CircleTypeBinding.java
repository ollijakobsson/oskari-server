package org.geotools.gml3.bindings;


import javax.xml.namespace.QName;

import org.geotools.gml3.ArcParameters;
import org.geotools.gml3.Circle;
import org.geotools.gml3.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;


/**
 *
 * @author Erik van de Pol. B3Partners BV.
 *
 *
 * @source $URL$
 */
public class X_CircleTypeBinding extends AbstractComplexBinding {
    GeometryFactory gFactory;
    CoordinateSequenceFactory csFactory;
    ArcParameters arcParameters;

    public X_CircleTypeBinding(GeometryFactory gFactory, CoordinateSequenceFactory csFactory, ArcParameters arcParameters) {
        this.gFactory = gFactory;
        this.csFactory = csFactory;
        this.arcParameters = arcParameters;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.CircleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return LineString.class;
    }

    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {

        LineString circleLineString = GML3ParsingUtils.lineString(node, gFactory, csFactory);

        Coordinate[] circleCoordinates = circleLineString.getCoordinates();
        if (circleCoordinates.length != 3) {
            // maybe log this instead and return null
            throw new RuntimeException(
                    "GML3 parser exception: The number of coordinates of a Circle should be 3. It currently is: " + circleCoordinates.length + "; " + circleLineString);
        }
        
        //System.err.println("ARC PARSE HACK");

        Coordinate c1 = circleCoordinates[0];
        Coordinate c2 = circleCoordinates[1];
        Coordinate c3 = circleCoordinates[2];

        Circle circle = new Circle(c1, c2, c3);
        double tolerance = arcParameters.getLinearizationTolerance().getTolerance(circle);
        Coordinate[] resultCoordinates = Circle.linearizeCircle(c1, c2, c3, tolerance);

        LineString resultLineString = gFactory.createLineString(resultCoordinates);

        return resultLineString;
    }

}

